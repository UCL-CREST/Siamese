    private static byte[] getLoginHashSHA(final char[] password, final int seed) throws GGException {
        try {
            final MessageDigest hash = MessageDigest.getInstance("SHA1");
            hash.update(new String(password).getBytes());
            hash.update(GGUtils.intToByte(seed));
            return hash.digest();
        } catch (final NoSuchAlgorithmException e) {
            LOG.error("SHA1 algorithm not usable", e);
            throw new GGException("SHA1 algorithm not usable!", e);
        }
    }
