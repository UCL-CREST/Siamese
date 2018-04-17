    @Override
    public void addApplication(Application app) {
        logger.info("Adding a new application " + app.getName() + " by " + app.getOrganisation() + " (" + app.getEmail() + ") ");
        app.setRegtime(new Timestamp(new Date().getTime()));
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update((app.getName() + app.getEmail() + app.getRegtime()).getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            app.setAppid(sb.toString());
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(ApplicationDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(app.toString());
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(app);
        Number appUid = insertApplication.executeAndReturnKey(parameters);
        app.setId(appUid.longValue());
    }
