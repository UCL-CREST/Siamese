    private List<Token> generateTokens(int tokenCount) throws XSServiceException {
        final List<Token> tokens = new ArrayList<Token>(tokenCount);
        final Random r = new Random();
        String t = Long.toString(new Date().getTime()) + Integer.toString(r.nextInt());
        final MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new XSServiceException("Error while creating tokens");
        }
        for (int i = 0; i < tokenCount; ++i) {
            final Token token = new Token();
            token.setValid(true);
            m.update(t.getBytes(), 0, t.length());
            String md5 = new BigInteger(1, m.digest()).toString(16);
            while (md5.length() < 32) {
                md5 = String.valueOf(r.nextInt(9)) + md5;
            }
            t = md5.substring(0, 8) + "-" + md5.substring(8, 16) + "-" + md5.substring(16, 24) + "-" + md5.substring(24, 32);
            logger.debug("Generated token #" + (i + 1) + ": " + t);
            token.setTokenString(t);
            tokens.add(token);
        }
        return tokens;
    }
