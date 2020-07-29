    private void createUser(AddEditUserForm addform, HttpServletRequest request, ActionMapping mapping) throws Exception {
        MessageDigest md = (MessageDigest) MessageDigest.getInstance("MD5").clone();
        md.update(addform.getPassword().getBytes("UTF-8"));
        byte[] pd = md.digest();
        StringBuffer app = new StringBuffer();
        for (int i = 0; i < pd.length; i++) {
            String s2 = Integer.toHexString(pd[i] & 0xFF);
            app.append((s2.length() == 1) ? "0" + s2 : s2);
        }
        Session hbsession = HibernateUtil.currentSession();
        try {
            Transaction tx = hbsession.beginTransaction();
            NvUsers user = new NvUsers();
            user.setLogin(addform.getLogin());
            user.setPassword(app.toString());
            hbsession.save(user);
            hbsession.flush();
            if (!hbsession.connection().getAutoCommit()) {
                tx.commit();
            }
        } finally {
            HibernateUtil.closeSession();
        }
    }
