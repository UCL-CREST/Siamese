    public static String getETag(final String uri, final long lastModified) {
        try {
            final MessageDigest dg = MessageDigest.getInstance("MD5");
            dg.update(uri.getBytes("utf-8"));
            dg.update(new byte[] { (byte) ((lastModified >> 24) & 0xFF), (byte) ((lastModified >> 16) & 0xFF), (byte) ((lastModified >> 8) & 0xFF), (byte) (lastModified & 0xFF) });
            return CBASE64Codec.encode(dg.digest());
        } catch (final Exception ignore) {
            return uri + lastModified;
        }
    }
