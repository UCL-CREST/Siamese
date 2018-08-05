    public void assign() throws Exception {
        if (proposalIds.equals("") || usrIds.equals("")) throw new Exception("No proposal or peer-viewer selected.");
        String[] pids = proposalIds.split(",");
        String[] uids = usrIds.split(",");
        int pnum = pids.length;
        int unum = uids.length;
        if (pnum == 0 || unum == 0) throw new Exception("No proposal or peer-viewer selected.");
        int i, j;
        String pStr = "update proposal set current_status='assigned' where ";
        for (i = 0; i < pnum; i++) {
            if (i > 0) pStr += " OR ";
            pStr += "PROPOSAL_ID=" + pids[i];
        }
        Calendar date = Calendar.getInstance();
        int day = date.get(Calendar.DATE);
        int month = date.get(Calendar.MONTH);
        int year = date.get(Calendar.YEAR);
        String dt = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
        PreparedStatement prepStmt = null;
        try {
            con = database.getConnection();
            con.setAutoCommit(false);
            prepStmt = con.prepareStatement(pStr);
            prepStmt.executeUpdate();
            pStr = "insert into event (summary,document1,document2,document3,publicComments,privateComments,ACTION_ID,eventDate,ROLE_ID,reviewText,USR_ID,PROPOSAL_ID,SUBJECTUSR_ID) values " + "('','','','','','','assigned','" + dt + "',2,'new'," + userId + ",?,?)";
            prepStmt = con.prepareStatement(pStr);
            for (i = 0; i < pnum; i++) {
                for (j = 0; j < unum; j++) {
                    prepStmt.setString(1, pids[i]);
                    prepStmt.setString(2, uids[j]);
                    prepStmt.executeUpdate();
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
        event_Form fr = new event_Form();
        for (j = 0; j < unum; j++) {
            fr.setUSR_ID(userId);
            fr.setSUBJECTUSR_ID(uids[j]);
            systemManager.handleEvent(SystemManager.EVENT_PROPOSAL_ASSIGNED, fr, null, null);
        }
    }
