    private static boolean validateSshaPwd(String sSshaPwd, String sUserPwd) {
        boolean b = false;
        if (sSshaPwd != null && sUserPwd != null) {
            if (sSshaPwd.startsWith(SSHA_PREFIX)) {
                sSshaPwd = sSshaPwd.substring(SSHA_PREFIX.length());
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA-1");
                    BASE64Decoder decoder = new BASE64Decoder();
                    byte[] ba = decoder.decodeBuffer(sSshaPwd);
                    byte[] hash = new byte[FIXED_HASH_SIZE];
                    byte[] salt = new byte[FIXED_SALT_SIZE];
                    System.arraycopy(ba, 0, hash, 0, FIXED_HASH_SIZE);
                    System.arraycopy(ba, FIXED_HASH_SIZE, salt, 0, FIXED_SALT_SIZE);
                    md.update(sUserPwd.getBytes());
                    md.update(salt);
                    byte[] baPwdHash = md.digest();
                    b = MessageDigest.isEqual(hash, baPwdHash);
                } catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }
        return b;
    }
