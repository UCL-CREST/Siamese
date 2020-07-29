    public static String calculateHash(String data, String algorithm) {
        if (data == null) {
            return null;
        }
        algorithm = (algorithm == null ? INTERNAL : algorithm.toUpperCase());
        if (algorithm.equals(PLAIN)) {
            return data;
        }
        if (algorithm.startsWith("{RSA}")) {
            return encode(data, algorithm.substring(5), "RSA");
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data.getBytes("UTF-8"));
            return getHashString(md.digest());
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            return null;
        } catch (NoSuchAlgorithmException nsae) {
            logger.error(nsae.getMessage());
            return null;
        }
    }
