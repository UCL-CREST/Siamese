    private static String encryptSHA1URL(String x) throws Exception {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x.getBytes());
        String passwd = "";
        passwd = URLEncoder.encode(new String(d.digest()), "ISO-8859-1");
        return passwd;
    }
