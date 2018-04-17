    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        StringBuffer encryptedPassword = new StringBuffer();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        md5.update(password.getBytes());
        byte digest[] = md5.digest();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1) {
                encryptedPassword.append('0');
            }
            encryptedPassword.append(hex);
        }
        return encryptedPassword.toString();
    }
