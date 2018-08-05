    public boolean checkUserExists(String username) {
        Context initCtx = null, envCtx = null;
        DataSource ds = null;
        Connection con = null;
        try {
            initCtx = new InitialContext();
            envCtx = (Context) initCtx.lookup("tbbs:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/tbbsDB");
            con = ds.getConnection();
            java.sql.Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery("SELECT userid FROM user_tbl WHERE userid = " + '"' + username + '"' + ";");
            return rset.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
