    private static void executeSQLScript() {
        File f = new File(System.getProperty("user.dir") + "/resources/umc.sql");
        if (f.exists()) {
            Connection con = null;
            PreparedStatement pre_stmt = null;
            try {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:database/umc.db", "", "");
                BufferedReader br = new BufferedReader(new FileReader(f));
                String line = "";
                con.setAutoCommit(false);
                while ((line = br.readLine()) != null) {
                    if (!line.equals("") && !line.startsWith("--") && !line.contains("--")) {
                        log.debug(line);
                        pre_stmt = con.prepareStatement(line);
                        pre_stmt.executeUpdate();
                    }
                }
                con.commit();
                File dest = new File(f.getAbsolutePath() + ".executed");
                if (dest.exists()) dest.delete();
                f.renameTo(dest);
                f.delete();
            } catch (Throwable exc) {
                log.error("Fehler bei Ausführung der SQL Datei", exc);
                try {
                    con.rollback();
                } catch (SQLException exc1) {
                }
            } finally {
                try {
                    if (pre_stmt != null) pre_stmt.close();
                    if (con != null) con.close();
                } catch (SQLException exc2) {
                    log.error("Fehler bei Ausführung von SQL Datei", exc2);
                }
            }
        }
    }
