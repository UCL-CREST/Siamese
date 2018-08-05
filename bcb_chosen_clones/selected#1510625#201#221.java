    public String plainStringToMD5(String input) {
        MessageDigest md = null;
        byte[] byteHash = null;
        StringBuffer resultString = new StringBuffer();
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.throwing(getClass().getName(), "plainStringToMD5", e);
        }
        md.reset();
        try {
            md.update(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
        }
        byteHash = md.digest();
        for (int i = 0; i < byteHash.length; i++) {
            resultString.append(Integer.toHexString(0xF0 & byteHash[i]).charAt(0));
            resultString.append(Integer.toHexString(0x0F & byteHash[i]));
        }
        return (resultString.toString());
    }
