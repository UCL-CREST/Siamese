    public LiferaySQL(String sql) {
        System.out.println(sql);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Properties props = new Properties();
            try {
                props.load(new FileInputStream(new File("db.properties")));
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            }
            String driver = props.getProperty("driver");
            String url = props.getProperty("url");
            String user = props.getProperty("user");
            String password = props.getProperty("password");
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            con.setAutoCommit(false);
            File sqlFile = new File(sql);
            if (sqlFile.exists()) {
                StringBuffer sb = new StringBuffer();
                BufferedReader br = new BufferedReader(new FileReader(sqlFile));
                String line = null;
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith("--")) {
                        sb.append(line);
                    }
                }
                br.close();
                StringTokenizer st = new StringTokenizer(sb.toString(), ";");
                while (st.hasMoreTokens()) {
                    line = st.nextToken();
                    System.out.println(line + ";");
                    ps = con.prepareStatement(line);
                    ps.executeUpdate();
                }
            } else if (sql.toLowerCase().startsWith("insert ") || sql.toLowerCase().startsWith("update ")) {
                ps = con.prepareStatement(sql);
                ps.executeUpdate(sql);
            } else {
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery(sql);
                ResultSetMetaData rsmd = rs.getMetaData();
                int[] width = new int[rsmd.getColumnCount() + 1];
                StringBuffer sb = new StringBuffer();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    width[i] = rsmd.getColumnLabel(i).length();
                }
                List results = new ArrayList();
                while (rs.next()) {
                    String[] rowResult = new String[rsmd.getColumnCount() + 1];
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        Object obj = rs.getObject(i);
                        if (obj != null) {
                            rowResult[i] = obj.toString();
                            int objWidth = obj.toString().length();
                            if (width[i] < objWidth) {
                                width[i] = objWidth;
                            }
                        } else {
                            rowResult[i] = "";
                        }
                    }
                    results.add(rowResult);
                }
                _printLine(rsmd, width, sb);
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    String label = rsmd.getColumnLabel(i);
                    sb.append("| ").append(label);
                    for (int j = 0; j <= width[i] - label.length(); j++) {
                        sb.append(" ");
                    }
                }
                sb.append("|\n");
                _printLine(rsmd, width, sb);
                Iterator itr = results.iterator();
                while (itr.hasNext()) {
                    String[] rowResult = (String[]) itr.next();
                    for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                        String s = rowResult[i];
                        sb.append("| ").append(s);
                        for (int j = 0; j <= width[i] - s.length(); j++) {
                            sb.append(" ");
                        }
                    }
                    sb.append("|\n");
                }
                _printLine(rsmd, width, sb);
                System.out.println(sb.toString());
            }
            con.commit();
        } catch (SQLException sqle) {
            while (sqle != null) {
                sqle.printStackTrace();
                sqle = sqle.getNextException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            _cleanUp(con, ps, rs);
        }
    }
