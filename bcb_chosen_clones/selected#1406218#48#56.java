    public static String generateHash(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException, DigestException {
        MessageDigest digest;
        digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(message.getBytes("iso-8859-1"), 0, message.length());
        byte[] output = new byte[20];
        digest.digest(output, 0, output.length);
        return convertToHex(output);
    }
