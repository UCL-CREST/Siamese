    public static String generateHash(final String sText, final String sAlgo) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance(sAlgo);
        md.update(sText.getBytes());
        final Formatter formatter = new Formatter();
        for (final Byte curByte : md.digest()) formatter.format("%x", curByte);
        return formatter.toString();
    }
