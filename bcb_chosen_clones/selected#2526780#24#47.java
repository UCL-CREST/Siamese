    public static String analyze(List<String> stackLines) {
        final MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        final Iterator<String> iterator = stackLines.iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        try {
            final String messageLine = iterator.next();
            final String exceptionClass = getExceptionClass(messageLine);
            messageDigest.update(exceptionClass.getBytes("UTF-8"));
            analyze(exceptionClass, iterator, messageDigest);
            final byte[] bytes = messageDigest.digest();
            final BigInteger bigInt = new BigInteger(1, bytes);
            final String ret = bigInt.toString(36);
            return ret;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
