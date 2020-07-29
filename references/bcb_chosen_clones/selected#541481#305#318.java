    public static String encryptePassword(String md5key, String passwordAccount, String encryptedPassword, int passwdenc) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(Constants.ALGORITHM);
        switch(passwdenc) {
            case 1:
                md.update((md5key + encryptedPassword).getBytes("8859_1"));
                break;
            case 2:
                md.update((encryptedPassword + md5key).getBytes("8859_1"));
                break;
            default:
                return null;
        }
        return new String(md.digest());
    }
