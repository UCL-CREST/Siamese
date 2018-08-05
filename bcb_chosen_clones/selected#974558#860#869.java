    private String getBytes(String in) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(in.getBytes());
        byte[] passWordBytes = md5.digest();
        String s = "[";
        for (int i = 0; i < passWordBytes.length; i++) s += passWordBytes[i] + ", ";
        s = s.substring(0, s.length() - 2);
        s += "]";
        return s;
    }
