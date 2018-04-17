    public void addAuthors() throws Exception {
        if (proposalIds.equals("") || usrIds.equals("")) throw new Exception("No proposal or author selected.");
        String[] pids = proposalIds.split(",");
        String[] uids = usrIds.split(",");
        int pnum = pids.length;
        int unum = uids.length;
        if (pnum == 0 || unum == 0) throw new Exception("No proposal or author selected.");
        int i, j;
        Calendar date = Calendar.getInstance();
        int day = date.get(Calendar.DATE);
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);
        String dt = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
        String pStr = "";
        PreparedStatement prepStmt = null;
        try {
            con = database.getConnection();
            con.setAutoCommit(false);
            pStr = "insert into event (summary,document1,document2,document3,publicComments,privateComments,ACTION_ID,eventDate,ROLE_ID,reviewText,USR_ID,PROPOSAL_ID,SUBJECTUSR_ID) values " + "('','','','','','','member added','" + dt + "',2,'add member'," + userId + ",?,?)";
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
            throw new Exception(e.getMessage() + "\n" + pStr + "\npnum=" + pnum + "\n" + pids[0] + "\nunum=" + unum + "\n" + uids[1] + uids[0]);
        }
    }
