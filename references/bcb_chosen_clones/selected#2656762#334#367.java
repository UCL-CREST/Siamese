    public void deleteType(String id) throws Exception {
        String tmp = "";
        PreparedStatement prepStmt = null;
        try {
            if (id == null || id.length() == 0) throw new Exception("Invalid parameter");
            con = database.getConnection();
            String delType = "delete from type where TYPE_ID='" + id + "'";
            con.setAutoCommit(false);
            prepStmt = con.prepareStatement("delete from correlation where TYPE_ID='" + id + "' OR CORRELATEDTYPE_ID='" + id + "'");
            prepStmt.executeUpdate();
            prepStmt = con.prepareStatement("delete from composition where TYPE_ID='" + id + "'");
            prepStmt.executeUpdate();
            prepStmt = con.prepareStatement("delete from distribution where TYPE_ID='" + id + "'");
            prepStmt.executeUpdate();
            prepStmt = con.prepareStatement("delete from typename where TYPE_ID='" + id + "'");
            prepStmt.executeUpdate();
            prepStmt = con.prepareStatement("delete from typereference where TYPE_ID='" + id + "'");
            prepStmt.executeUpdate();
            prepStmt = con.prepareStatement("delete from plot where TYPE_ID='" + id + "'");
            prepStmt.executeUpdate();
            prepStmt = con.prepareStatement(delType);
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
