    public boolean remove() {
        io.clearScreen();
        io.textColor(io.fg_white + io.bg_black);
        io.print("Confirm delete ((Y)es  (N)o)? ");
        char choice = getMenuSelection();
        if (choice == 'N' || choice == 'n') return false;
        Context initCtx = null;
        Context envCtx = null;
        DataSource ds = null;
        Connection con = null;
        try {
            initCtx = new InitialContext();
            envCtx = (Context) initCtx.lookup("tbbs:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/tbbsDB");
            con = ds.getConnection();
            con.setAutoCommit(false);
            java.sql.Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT tid FROM topic_tbl " + "WHERE tid = " + tid + ";");
            if (!rs.next()) return false;
            stmt.executeUpdate("DELETE FROM message_tbl WHERE tid = " + tid + ";");
            stmt.executeUpdate("DELETE FROM topic_tbl WHERE tid = " + tid + ";");
            con.commit();
            con.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
