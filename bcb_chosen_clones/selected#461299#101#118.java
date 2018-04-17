    private static String md5(String digest, String data) throws IOException {
        MessageDigest messagedigest;
        try {
            messagedigest = MessageDigest.getInstance(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        messagedigest.update(data.getBytes("ISO-8859-1"));
        byte[] bytes = messagedigest.digest();
        StringBuilder stringbuffer = new StringBuilder(bytes.length * 2);
        for (int j = 0; j < bytes.length; j++) {
            int k = bytes[j] >>> 4 & 0x0f;
            stringbuffer.append(hexChars[k]);
            k = bytes[j] & 0x0f;
            stringbuffer.append(hexChars[k]);
        }
        return stringbuffer.toString();
    }
