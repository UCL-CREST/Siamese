    public static String hash(String in, String algorithm) {
        if (StringUtils.isBlank(algorithm)) algorithm = DEFAULT_ALGORITHM;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException nsae) {
            logger.error("No such algorithm exception", nsae);
        }
        md.reset();
        md.update(in.getBytes());
        String out = null;
        try {
            out = Base64Encoder.encode(md.digest());
        } catch (IOException e) {
            logger.error("Error converting to Base64 ", e);
        }
        if (out.endsWith("\n")) out = out.substring(0, out.length() - 1);
        return out;
    }
