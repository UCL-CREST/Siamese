    public static String getUserPass(String user) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(user.getBytes());
        byte[] hash = digest.digest();
        System.out.println("Returning user pass:" + hash);
        return hash.toString();
    }
