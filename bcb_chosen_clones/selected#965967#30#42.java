    public static String MD5_hex(String p) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(p.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            String ret = hash.toString(16);
            return ret;
        } catch (NoSuchAlgorithmException e) {
            logger.error("can not create confirmation key", e);
            throw new TechException(e);
        }
    }
