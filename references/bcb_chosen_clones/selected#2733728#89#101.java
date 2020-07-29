    private static byte[] createHash(EHashType hashType, String string) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(hashType.getJavaHashType());
            md.reset();
            md.update(string.getBytes());
            byte[] byteResult = md.digest();
            return byteResult;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
