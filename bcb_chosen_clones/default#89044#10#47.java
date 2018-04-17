    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Connection con;
        String dbsource = "jdbc:odbc:jdbctest";
        String drv = "sun.jdbc.odbc.JdbcOdbcDriver";
        String duser = "root";
        String dpass = "";
        String defaultquery = "select * from student";
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<html><head><title>Student File</title></head><body bgcolor=\"turquoise\"><h3>Student File</h3>");
        try {
            con = DriverManager.getConnection(dbsource, duser, dpass);
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(defaultquery);
            out.println("<form method=\"POST\"><input type=\"submit\" value=\"Add Record\" name=\"add\"></form><center><hr>");
            out.println("<table border=\"1\">");
            out.println("<th>Name</th>");
            out.println("<th>Address</th>");
            out.println("<th>Phone</th>");
            out.println("<th></th>");
            while (rs.next()) {
                String val1 = rs.getString(1);
                String val2 = rs.getString(2);
                String val3 = rs.getString(3);
                out.println("<tr>");
                out.println("<td>" + val1 + "</td>");
                out.println("<td>" + val2 + "</td>");
                out.println("<td>" + val3 + "</td>");
                out.println("<td><form method=\"POST\"><input type=\"hidden\" name=\"name\" value=\"" + val1 + "\"><input type=\"hidden\" name=\"address\" value=\"" + val2 + "\"><input type=\"hidden\" name=\"phone\" value=\"" + val3 + "\"><input type=\"submit\" value=\"Update\" name=\"update\"><input type=\"submit\" value=\"Delete\" name=\"delete\"></form></td>");
                out.println("</tr>");
            }
            out.println("</table>");
            con.close();
        } catch (SQLException ex) {
            System.out.println("SQL EXCEPTION:" + ex.getMessage());
        }
        out.println("</center></body></html>");
    }
