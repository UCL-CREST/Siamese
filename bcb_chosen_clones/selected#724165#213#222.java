    public static String getHash(String uri) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("MD5");
        mDigest.update(uri.getBytes());
        byte d[] = mDigest.digest();
        StringBuffer hash = new StringBuffer();
        for (int i = 0; i < d.length; i++) {
            hash.append(Integer.toHexString(0xFF & d[i]));
        }
        return hash.toString();
    }
