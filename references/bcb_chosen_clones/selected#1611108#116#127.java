    public static String cryptoSHA(String _strSrc) {
        try {
            BASE64Encoder encoder = new BASE64Encoder();
            MessageDigest sha = MessageDigest.getInstance("SHA");
            sha.update(_strSrc.getBytes());
            byte[] buffer = sha.digest();
            return encoder.encode(buffer);
        } catch (Exception err) {
            System.out.println(err);
        }
        return "";
    }
