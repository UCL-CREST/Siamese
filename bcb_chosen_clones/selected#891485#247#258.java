    private static String md5Encode(String pass) {
        String string;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
            byte[] result = md.digest();
            string = bytes2hexStr(result);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("La libreria java.security no implemente MD5");
        }
        return string;
    }
