    private static String getVisitorId(String guid, String account, String userAgent, Cookie cookie) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (cookie != null && cookie.getValue() != null) {
            return cookie.getValue();
        }
        String message;
        if (!isEmpty(guid)) {
            message = guid + account;
        } else {
            message = userAgent + getRandomNumber() + UUID.randomUUID().toString();
        }
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(message.getBytes("UTF-8"), 0, message.length());
        byte[] sum = m.digest();
        BigInteger messageAsNumber = new BigInteger(1, sum);
        String md5String = messageAsNumber.toString(16);
        while (md5String.length() < 32) {
            md5String = "0" + md5String;
        }
        return "0x" + md5String.substring(0, 16);
    }
