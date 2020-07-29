    public static void processString(String text) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(MD5_DIGEST);
        md5.reset();
        md5.update(text.getBytes());
        displayResult(null, md5.digest());
    }
