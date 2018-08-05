    private String md5(String txt) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(txt.getBytes(), 0, txt.length());
            return new BigInteger(1, m.digest()).toString(16);
        } catch (Exception e) {
            return "BAD MD5";
        }
    }
