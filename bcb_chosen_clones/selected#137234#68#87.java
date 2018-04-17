    public static String mysqlPasswordHash(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HashAlgorithms.SHA1);
            try {
                digest.update(string.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            byte[] encodedPassword = digest.digest();
            digest.update(encodedPassword);
            encodedPassword = digest.digest();
            String hash = new BigInteger(1, encodedPassword).toString(16).toUpperCase();
            while (hash.length() < 40) {
                hash = "0" + hash;
            }
            return "*" + hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
