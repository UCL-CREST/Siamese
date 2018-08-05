    public static byte[] getHashedPassword(String password, byte[] randomBytes) {
        byte[] hashedPassword = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(randomBytes);
            messageDigest.update(password.getBytes("UTF-8"));
            hashedPassword = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }
