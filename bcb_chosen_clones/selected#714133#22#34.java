    private String hashKey(String key) {
        String hashed = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(key.getBytes());
            BigInteger hash = new BigInteger(1, md5.digest());
            hashed = hash.toString(16);
        } catch (Exception ex) {
            ex.printStackTrace();
            hashed = String.valueOf(key.hashCode());
        }
        return hashed;
    }
