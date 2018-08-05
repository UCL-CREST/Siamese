    public void deleteAuthors() throws Exception {
        if (proposalIds.equals("") || usrIds.equals("")) throw new Exception("No proposal or author selected.");
        String[] pids = proposalIds.split(",");
        String[] uids = usrIds.split(",");
        int pnum = pids.length;
        int unum = uids.length;
        if (pnum == 0 || unum == 0) throw new Exception("No proposal or author selected.");
        int i, j;
        PreparedStatement prepStmt = null;
        try {
            con = database.getConnection();
            con.setAutoCommit(false);
            String pStr = "delete from event where ACTION_ID='member added' AND PROPOSAL_ID=? AND SUBJECTUSR_ID=?";
            prepStmt = con.prepareStatement(pStr);
            for (i = 0; i < pnum; i++) {
                for (j = 0; j < unum; j++) {
                    if (!uids[j].equals(userId)) {
                        prepStmt.setString(1, pids[i]);
                        prepStmt.setString(2, uids[j]);
                        prepStmt.executeUpdate();
                    }
                }
            }
            con.commit();
        } catch (Exception e) {
            if (!con.isClosed()) {
                con.rollback();
                prepStmt.close();
                con.close();
            }
            throw e;
        }
    }
