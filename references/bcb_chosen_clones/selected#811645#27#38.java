    public static String encryptPass2(String pass) throws UnsupportedEncodingException {
        String passEncrypt;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
        }
        md5.update(pass.getBytes());
        String dis = new String(md5.digest(), 10);
        passEncrypt = dis.toString();
        return passEncrypt;
    }
