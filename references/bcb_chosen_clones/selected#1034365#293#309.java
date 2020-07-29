    public static String generateHexadecimalCodedString(String stringToBeCoded) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        digest.update(stringToBeCoded.getBytes());
        byte[] hashedKey = digest.digest();
        final int radix = 16;
        String result = "";
        for (byte b : hashedKey) {
            int unsignedByte = b + 128;
            result += Integer.toString(unsignedByte, radix);
        }
        return result;
    }
