    public void deleteSingle(String tbName, String idFld, String id) throws Exception {
        String tmp = "";
        PreparedStatement prepStmt = null;
        try {
            if (tbName == null || tbName.length() == 0 || id == null || id.length() == 0) throw new Exception("Invalid parameter");
            con = database.getConnection();
            String delSQL = "delete from " + tbName + " where " + idFld + "='" + id + "'";
            con.setAutoCommit(false);
            prepStmt = con.prepareStatement(delSQL);
            prepStmt.executeUpdate();
            con.commit();
            prepStmt.close();
            con.close();
        } catch (Exception e) {
            if (!con.isClosed()) {
                con.rollback();
                prepStmt.close();
                con.close();
            }
            throw e;
        }
    }
