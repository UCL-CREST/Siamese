    private void loadMessages() {
        messages = new Vector();
        Context initCtx = null;
        Context envCtx = null;
        DataSource ds = null;
        Connection con = null;
        try {
            initCtx = new InitialContext();
            envCtx = (Context) initCtx.lookup("tbbs:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/tbbsDB");
            con = ds.getConnection();
            java.sql.Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT mid FROM message_tbl " + "WHERE tid = " + tid + ";");
            while (rs.next()) messages.add(new Message(rs.getInt("mid")));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (currentMessage > messages.size() - 1) currentMessage = messages.size() - 1;
        if (currentMessage < 0) currentMessage = 0;
    }
