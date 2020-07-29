    private static boolean execute(String query) throws SQLException {
        boolean success = true;
        try {
            PreparedStatement stm = con.prepareStatement(query);
            stm.executeUpdate();
            stm.close();
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (Exception rbex) {
                rbex.printStackTrace();
            }
            success = false;
            throw e;
        }
        return success;
    }
