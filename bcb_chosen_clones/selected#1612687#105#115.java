    public static String digest(String text, String algorithm, String charsetName) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(text.getBytes(charsetName), 0, text.length());
            return convertToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("unexpected exception: " + e, e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("unexpected exception: " + e, e);
        }
    }
