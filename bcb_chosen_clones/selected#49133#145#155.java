    public static String calculateHash(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.reset();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        md.update(password.getBytes());
        return byteToBase64(md.digest());
    }
