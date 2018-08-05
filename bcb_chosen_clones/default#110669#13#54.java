    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        out.println("<p>Tomcat-Tyrex Integration Test");
        try {
            InitialContext ctx;
            UserTransaction ut;
            DataSource ds;
            Connection conn;
            Statement st;
            ResultSet rs;
            ctx = new InitialContext();
            ut = (UserTransaction) ctx.lookup("java:/comp/UserTransaction");
            ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/mydb");
            conn = ds.getConnection();
            ut.begin();
            st = conn.createStatement();
            rs = st.executeQuery("SELECT text FROM test WHERE id=1");
            if (rs.next()) {
                out.println("<p>Current value: " + rs.getString(1));
                st.executeUpdate("UPDATE test SET text='" + ctx.lookup("java:/comp/env/text") + "' WHERE id=1");
            } else {
                out.println("<p>Current value: null");
                st.executeUpdate("INSERT into test values (1," + "'" + ctx.lookup("java:/comp/env/text") + "')");
            }
            rs.close();
            rs = st.executeQuery("SELECT text FROM test WHERE id=1");
            rs.next();
            out.println("<p>Updated to value: " + rs.getString(1));
            ut.rollback();
            rs = st.executeQuery("SELECT text FROM test WHERE id=1");
            if (rs.next()) {
                out.println("<p>Rolledback to value: " + rs.getString(1));
            } else {
                out.println("<p>Rolledback to value: null");
            }
        } catch (Throwable except) {
            out.println("<pre>except");
            except.printStackTrace(out);
            out.println("</pre>");
        }
    }
