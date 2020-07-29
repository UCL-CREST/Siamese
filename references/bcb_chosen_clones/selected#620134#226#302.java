    private static void executeDBPatchFile() throws Exception {
        Connection con = null;
        PreparedStatement pre_stmt = null;
        ResultSet rs = null;
        try {
            InputStream is = null;
            URL url = new URL("http://www.hdd-player.de/umc/UMC-DB-Update-Script.sql");
            is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:database/umc.db", "", "");
            double dbVersion = -1;
            pre_stmt = con.prepareStatement("SELECT * FROM DB_VERSION WHERE ID_MODUL = 0");
            rs = pre_stmt.executeQuery();
            if (rs.next()) {
                dbVersion = rs.getDouble("VERSION");
            }
            String line = "";
            con.setAutoCommit(false);
            boolean collectSQL = false;
            ArrayList<String> sqls = new ArrayList<String>();
            double patchVersion = 0;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("[")) {
                    Pattern p = Pattern.compile("\\[.*\\]");
                    Matcher m = p.matcher(line);
                    m.find();
                    String value = m.group();
                    value = value.substring(1, value.length() - 1);
                    patchVersion = Double.parseDouble(value);
                }
                if (patchVersion == dbVersion + 1) collectSQL = true;
                if (collectSQL) {
                    if (!line.equals("") && !line.startsWith("[") && !line.startsWith("--") && !line.contains("--")) {
                        if (line.endsWith(";")) line = line.substring(0, line.length() - 1);
                        sqls.add(line);
                    }
                }
            }
            if (pre_stmt != null) pre_stmt.close();
            if (rs != null) rs.close();
            for (String sql : sqls) {
                log.debug("Führe SQL aus Patch Datei aus: " + sql);
                pre_stmt = con.prepareStatement(sql);
                pre_stmt.execute();
            }
            if (patchVersion > 0) {
                log.debug("aktualisiere Versionsnummer in DB");
                if (pre_stmt != null) pre_stmt.close();
                if (rs != null) rs.close();
                pre_stmt = con.prepareStatement("UPDATE DB_VERSION SET VERSION = ? WHERE ID_MODUL = 0");
                pre_stmt.setDouble(1, patchVersion);
                pre_stmt.execute();
            }
            con.commit();
        } catch (MalformedURLException exc) {
            log.error(exc.toString());
            throw new Exception("SQL Patch Datei konnte nicht online gefunden werden", exc);
        } catch (IOException exc) {
            log.error(exc.toString());
            throw new Exception("SQL Patch Datei konnte nicht gelesen werden", exc);
        } catch (Throwable exc) {
            log.error("Fehler bei Ausführung der SQL Patch Datei", exc);
            try {
                con.rollback();
            } catch (SQLException exc1) {
            }
            throw new Exception("SQL Patch Datei konnte nicht ausgeführt werden", exc);
        } finally {
            try {
                if (pre_stmt != null) pre_stmt.close();
                if (con != null) con.close();
            } catch (SQLException exc2) {
                log.error("Fehler bei Ausführung von SQL Patch Datei", exc2);
            }
        }
    }
