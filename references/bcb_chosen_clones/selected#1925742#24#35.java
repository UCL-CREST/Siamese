    private static byte[] getHashBytes(String data, String algorithm) {
        MessageDigest md;
        byte[] digest = null;
        try {
            md = MessageDigest.getInstance(algorithm);
            md.update(data.getBytes("UTF-8"), 0, data.length());
            digest = md.digest();
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return digest;
    }
