    public static boolean matchPassword(String prevPassStr, String newPassword) throws NoSuchAlgorithmException, java.io.IOException, java.io.UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] seed = new byte[12];
        byte[] prevPass = new sun.misc.BASE64Decoder().decodeBuffer(prevPassStr);
        System.arraycopy(prevPass, 0, seed, 0, 12);
        md.update(seed);
        md.update(newPassword.getBytes("UTF8"));
        byte[] digestNewPassword = md.digest();
        byte[] choppedPrevPassword = new byte[prevPass.length - 12];
        System.arraycopy(prevPass, 12, choppedPrevPassword, 0, prevPass.length - 12);
        boolean isMatching = Arrays.equals(digestNewPassword, choppedPrevPassword);
        return isMatching;
    }
