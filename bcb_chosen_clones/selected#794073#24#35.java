    public static String hash(final String text) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            byte[] sha1hash = new byte[40];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha1hash = md.digest();
            return Sha1.convertToHex(sha1hash);
        } catch (final Exception e) {
            return null;
        }
    }
