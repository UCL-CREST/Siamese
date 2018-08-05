    public static String crypt(String passwd, boolean pad) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(passwd.getBytes());
            String c = new String(sha.digest());
            return toNumeric(c, pad, true);
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.error(Login.class, "couldn't crypt()", e);
            return "";
        }
    }
