    public final String encrypt(String input) throws Exception {
        try {
            MessageDigest messageDigest = (MessageDigest) MessageDigest.getInstance(algorithm).clone();
            messageDigest.reset();
            messageDigest.update(input.getBytes());
            String output = convert(messageDigest.digest());
            return output;
        } catch (Throwable ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Fatal Error while digesting input string", ex);
            }
        }
        return input;
    }
