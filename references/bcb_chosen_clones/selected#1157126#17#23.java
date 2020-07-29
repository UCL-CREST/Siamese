    public static String encrypt(String txt) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(txt.getBytes("UTF-8"));
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }
