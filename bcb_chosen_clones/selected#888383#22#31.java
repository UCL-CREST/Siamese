    public static String hexHash(Object obj) {
        String toHash = obj.toString();
        try {
            MessageDigest dg = MessageDigest.getInstance("MD5");
            dg.update(toHash.getBytes("UTF-8"));
            return bytesToHex(dg.digest());
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing string: " + toHash, e);
        }
    }
