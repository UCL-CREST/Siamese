    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        PrintWriter out = res.getWriter();
        String requestNumber = req.getParameter("reqno");
        int parseNumber = Integer.parseInt(requestNumber);
        Connection con = null;
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            con = DriverManager.getConnection("jdbc:derby:/DerbyDB/AssetDB");
            con.setAutoCommit(false);
            String inet = req.getRemoteAddr();
            Statement stmt = con.createStatement();
            String sql = "UPDATE REQUEST_DETAILS SET viewed = '1', checked_by = '" + inet + "' WHERE QUERY = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, parseNumber);
            pst.executeUpdate();
            con.commit();
            String nextJSP = "/queryRemoved.jsp";
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
            dispatcher.forward(req, res);
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (SQLException ignored) {
            }
            out.println("Failed");
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException ignored) {
            }
        }
    }
