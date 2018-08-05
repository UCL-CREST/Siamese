    @SuppressWarnings("unused")
    private static int chkPasswd(final String sInputPwd, final String sSshaPwd) {
        assert sInputPwd != null;
        assert sSshaPwd != null;
        int r = ERR_LOGIN_ACCOUNT;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] ba = decoder.decodeBuffer(sSshaPwd);
            assert ba.length >= FIXED_HASH_SIZE;
            byte[] hash = new byte[FIXED_HASH_SIZE];
            byte[] salt = new byte[FIXED_SALT_SIZE];
            System.arraycopy(ba, 0, hash, 0, FIXED_HASH_SIZE);
            System.arraycopy(ba, FIXED_HASH_SIZE, salt, 0, FIXED_SALT_SIZE);
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(sInputPwd.getBytes());
            md.update(salt);
            byte[] baPwdHash = md.digest();
            if (MessageDigest.isEqual(hash, baPwdHash)) {
                r = ERR_LOGIN_OK;
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return r;
    }
