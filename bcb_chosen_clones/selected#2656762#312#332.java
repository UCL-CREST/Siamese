    public void deleteProposal(String id) throws Exception {
        String tmp = "";
        PreparedStatement prepStmt = null;
        try {
            if (id == null || id.length() == 0) throw new Exception("Invalid parameter");
            con = database.getConnection();
            String delProposal = "delete from proposal where PROPOSAL_ID='" + id + "'";
            prepStmt = con.prepareStatement(delProposal);
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
