    public static final String MD5(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            String newValue = hash.toString(16);
            return newValue;
        } catch (NoSuchAlgorithmException ns) {
            ns.printStackTrace();
            return null;
        }
    }
