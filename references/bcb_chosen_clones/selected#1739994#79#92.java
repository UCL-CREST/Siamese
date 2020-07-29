    public static String login() throws Exception {
        if (sid == null) {
            String login = ROLAPClientAux.loadProperties().getProperty("user");
            String password = ROLAPClientAux.loadProperties().getProperty("password");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            String password2 = asHexString(md.digest());
            String query = "/server/login?user=" + login + "&extern_password=" + password + "&password=" + password2;
            Vector<String> res = ROLAPClientAux.sendRequest(query);
            String vals[] = res.get(0).split(";");
            sid = vals[0];
        }
        return sid;
    }
