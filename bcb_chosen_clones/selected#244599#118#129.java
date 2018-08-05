    public static byte[] hash(final byte[] saltBefore, final String content, final byte[] saltAfter, final int repeatedHashingCount) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (content == null) return null;
        final MessageDigest digest = MessageDigest.getInstance(DIGEST);
        if (digestLength == -1) digestLength = digest.getDigestLength();
        for (int i = 0; i < repeatedHashingCount; i++) {
            if (i > 0) digest.update(digest.digest());
            digest.update(saltBefore);
            digest.update(content.getBytes(WebCastellumFilter.DEFAULT_CHARACTER_ENCODING));
            digest.update(saltAfter);
        }
        return digest.digest();
    }
