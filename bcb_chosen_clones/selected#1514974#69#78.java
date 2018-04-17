    private byte[] hash(String toHash) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5", "BC");
            md5.update(toHash.getBytes("ISO-8859-1"));
            return md5.digest();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
