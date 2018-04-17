    private final String createMD5(String pwd) throws Exception {
        MessageDigest md = (MessageDigest) MessageDigest.getInstance("MD5").clone();
        md.update(pwd.getBytes("UTF-8"));
        byte[] pd = md.digest();
        StringBuffer app = new StringBuffer();
        for (int i = 0; i < pd.length; i++) {
            String s2 = Integer.toHexString(pd[i] & 0xFF);
            app.append((s2.length() == 1) ? "0" + s2 : s2);
        }
        return app.toString();
    }
