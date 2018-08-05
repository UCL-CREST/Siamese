    public static String encryptPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digester = MessageDigest.getInstance("sha-256");
        digester.reset();
        digester.update("Carmen Sandiago".getBytes());
        return asHex(digester.digest(password.getBytes("UTF-8")));
    }
