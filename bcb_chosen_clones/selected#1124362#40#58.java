    public static String plainToMD(LoggerCollection loggerCol, String input) {
        byte[] byteHash = null;
        MessageDigest md = null;
        StringBuilder md4result = new StringBuilder();
        try {
            md = MessageDigest.getInstance("MD4", new BouncyCastleProvider());
            md.reset();
            md.update(input.getBytes("UnicodeLittleUnmarked"));
            byteHash = md.digest();
            for (int i = 0; i < byteHash.length; i++) {
                md4result.append(Integer.toHexString(0xFF & byteHash[i]));
            }
        } catch (UnsupportedEncodingException ex) {
            loggerCol.logException(CLASSDEBUG, "de.searchworkorange.lib.misc.hash.MD4Hash", Level.FATAL, ex);
        } catch (NoSuchAlgorithmException ex) {
            loggerCol.logException(CLASSDEBUG, "de.searchworkorange.lib.misc.hash.MD4Hash", Level.FATAL, ex);
        }
        return (md4result.toString());
    }
