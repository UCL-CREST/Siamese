    public static void refreshSession(int C_ID) {
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement updateLogin = con.prepareStatement("UPDATE customer SET c_login = NOW(), c_expiration = DATE_ADD(NOW(), INTERVAL 2 HOUR) WHERE c_id = ?");
            updateLogin.setInt(1, C_ID);
            updateLogin.executeUpdate();
            con.commit();
            updateLogin.close();
            returnConnection(con);
        } catch (java.lang.Exception ex) {
            try {
                con.rollback();
                ex.printStackTrace();
            } catch (Exception se) {
                System.err.println("Transaction rollback failed.");
            }
        }
    }
