    public static String toMd5(String s) {
        String res = "";
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            res = toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
        }
        return res;
    }
