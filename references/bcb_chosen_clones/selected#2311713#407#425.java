    public static String getSHADigest(String input) {
        if (input == null) return null;
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (java.security.NoSuchAlgorithmException nsae) {
            throw new RuntimeException(nsae);
        }
        if (sha == null) throw new RuntimeException("No message digest");
        sha.update(input.getBytes());
        byte[] data = sha.digest();
        StringBuffer buf = new StringBuffer(data.length * 2);
        for (int i = 0; i < data.length; i++) {
            int value = data[i] & 0xff;
            buf.append(hexDigit(value >> 4));
            buf.append(hexDigit(value));
        }
        return buf.toString();
    }
