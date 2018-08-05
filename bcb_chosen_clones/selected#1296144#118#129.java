    public static String stringToHash(String text) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Should not happened: SHA-1 algorithm is missing.");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Should not happened: Could not encode text bytes '" + text + "' to iso-8859-1.");
        }
        return new String(Base64.encodeBase64(md.digest()));
    }
