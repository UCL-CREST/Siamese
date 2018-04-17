    public static String GetMD5SUM(String s) throws NoSuchAlgorithmException {
        MessageDigest algorithm = MessageDigest.getInstance("MD5");
        algorithm.reset();
        algorithm.update(s.getBytes());
        byte messageDigest[] = algorithm.digest();
        String md5sum = Base64.encode(messageDigest);
        return md5sum;
    }
