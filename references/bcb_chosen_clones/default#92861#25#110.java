    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            String command_state = "";
            String device_state = "";
            String cD = "";
            String tD = "";
            String tE = "";
            String sqlReq = "SELECT now(),* FROM cmnds order by id desc limit 1;";
            try {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://localhost:5432/gisdb_fps";
                String username = "postgres";
                String password = "pgsql";
                Connection con = DriverManager.getConnection(url, username, password);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sqlReq);
                while (rs.next()) {
                    for (int i = 1; i < 6; i++) {
                        String temp = "";
                        try {
                            temp = rs.getString(i).trim();
                            if (i == 3) {
                                tD = temp;
                            }
                            if (i == 4) {
                                tE = temp;
                            }
                        } catch (Exception e) {
                        }
                        ;
                        command_state += "|" + temp;
                    }
                }
                rs.close();
                st.close();
                con.close();
            } catch (Exception e) {
                out.print("Exception select:" + e);
            }
            sqlReq = "SELECT id, srvtime, dvctime, orient1, orient2, orient3, X(\"position\"), Y(\"position\"),ext FROM timeline order by id desc limit 1;";
            try {
                Class.forName("org.postgresql.Driver");
                String url = "jdbc:postgresql://localhost:5432/gisdb_fps";
                String username = "postgres";
                String password = "pgsql";
                Connection con = DriverManager.getConnection(url, username, password);
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sqlReq);
                while (rs.next()) {
                    for (int i = 1; i < 9; i++) {
                        String temp = "";
                        try {
                            temp = rs.getString(i).trim();
                            if (i == 4) {
                                cD = temp;
                            }
                        } catch (Exception e) {
                        }
                        ;
                        device_state += "|" + temp;
                    }
                }
                rs.close();
                st.close();
                con.close();
            } catch (Exception e) {
                out.print("Exception select:" + e);
            }
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet cmnd</title>");
            out.println("<meta http-equiv=\"refresh\" content=\"3\">");
            out.println("</head>");
            out.println("<body>");
            out.println("device and command state: <br/>");
            out.println(command_state + "|" + device_state);
            out.println("<br/> tD|cD|tE <br/>");
            out.println(tD + " | " + cD + " | " + tE + " <br/>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }
