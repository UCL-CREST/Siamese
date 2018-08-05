    public void writeOut() {
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
            ResultSet rs = stmt.executeQuery("SELECT tid FROM topic_tbl;");
            tid = 0;
            while (rs.next()) {
                if (rs.getInt("tid") == tid) ++tid; else break;
            }
            PreparedStatement insMB = con.prepareStatement("INSERT INTO topic_tbl VALUES(?, ?, ?, 1);");
            insMB.setInt(1, tid);
            insMB.setString(2, mb);
            insMB.setString(3, topic);
            insMB.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
