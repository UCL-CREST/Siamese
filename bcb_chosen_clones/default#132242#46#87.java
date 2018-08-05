    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.print("<html><body>");
        String cCode = req.getParameter("categoryCode").trim();
        String cName = req.getParameter("categoryName").trim();
        String subCatCode = req.getParameter("subCategoryCode").trim();
        boolean proceed = false;
        if (cCode != null && cName != null && subCatCode != null) if (cCode.length() > 0 && cName.length() > 0 && subCatCode != null) proceed = true;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://localhost/inventory";
            conn = DriverManager.getConnection(url, "root", "");
            sql = "INSERT INTO category(category_code, category_name, subcategory_code) VALUES(?, ?, ?)";
            ps = conn.prepareStatement(sql);
            if (proceed) {
                ps.setString(1, cCode);
                ps.setString(2, cName);
                ps.setString(3, subCatCode);
                ps.executeUpdate();
            }
            out.print("You have added a new category named ");
            out.print(cName);
        } catch (ClassNotFoundException cnfe) {
            out.println("" + cnfe);
        } catch (SQLException sqle) {
            out.println("" + sqle);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                out.println("" + sqle);
            }
        }
        out.print("</body></html>");
        out.close();
    }
