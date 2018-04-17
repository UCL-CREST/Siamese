    public static byte[] MD5(String... strings) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            for (String string : strings) {
                digest.update(string.getBytes("UTF-8"));
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.toString(), e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.toString(), e);
        }
    }
