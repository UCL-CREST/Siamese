    public long getMD5Hash(String str) {
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(str.getBytes(), 0, str.length());
        return new BigInteger(1, m.digest()).longValue();
    }
