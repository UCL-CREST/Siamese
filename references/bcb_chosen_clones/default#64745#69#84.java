    public String encryptPassword(String clearPassword) throws NullPointerException {
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new NullPointerException("NoSuchAlgorithmException: " + e.toString());
        }
        sha.update(clearPassword.getBytes());
        byte encryptedPassword[] = sha.digest();
        sha = null;
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < encryptedPassword.length; i++) {
            result.append(Byte.toString(encryptedPassword[i]));
        }
        return (result.toString());
    }
