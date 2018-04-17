    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if (req.getParameter("delete") != null) {
            deletePage(req, res);
        }
        if (req.getParameter("deleteval") != null) {
            try {
                PrintWriter o = res.getWriter();
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://localhost/inventory";
                con = DriverManager.getConnection(url, "root", "");
                String delename = req.getParameter("delname");
                PreparedStatement st = con.prepareStatement("delete from student where name='" + delename + "'");
                int pe = st.executeUpdate();
                res.setContentType("text/html");
                res.setHeader("pragma", "no-cache");
                o.print("<HTML><HEAD><TITLE>Exercise2.1</TITLE></HEAD><BODY>" + "<h2>Deleted record!!</h2>");
            } catch (Exception e) {
                PrintWriter o = res.getWriter();
                o.println("Error statement");
                e.printStackTrace();
            }
        }
    }
