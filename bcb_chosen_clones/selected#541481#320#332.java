    public static boolean checkEncryptedPassword(String md5key, String passwordAccount, String encryptedPassword, int passwdenc) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(Constants.ALGORITHM);
        switch(passwdenc) {
            case 1:
                md.update((md5key + encryptedPassword).getBytes("8859_1"));
                return md.digest().equals(passwordAccount.getBytes("8859_1"));
            case 2:
                md.update((encryptedPassword + md5key).getBytes("8859_1"));
                return md.digest().equals(passwordAccount.getBytes("8859_1"));
            default:
                return false;
        }
    }
