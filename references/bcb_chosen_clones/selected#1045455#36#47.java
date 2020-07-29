    public static byte[] generateAuthId(String userName, String password) {
        byte[] ret = new byte[16];
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            String str = userName + password;
            messageDigest.update(str.getBytes());
            ret = messageDigest.digest();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
