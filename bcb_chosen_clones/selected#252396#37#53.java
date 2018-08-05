    public static String plainToMD(LoggerCollection loggerCol, String input) {
        byte[] byteHash = null;
        MessageDigest md = null;
        StringBuilder md5result = new StringBuilder();
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(input.getBytes());
            byteHash = md.digest();
            for (int i = 0; i < byteHash.length; i++) {
                md5result.append(Integer.toHexString(0xFF & byteHash[i]));
            }
        } catch (NoSuchAlgorithmException ex) {
            loggerCol.logException(CLASSDEBUG, "de.searchworkorange.lib.misc.hash.MD5Hash", Level.FATAL, ex);
        }
        return (md5result.toString());
    }
