    public static String hashClientPassword(String algorithm, String password, String salt) throws IllegalArgumentException, DruidSafeRuntimeException {
        if (algorithm == null) {
            throw new IllegalArgumentException("THE ALGORITHM MUST NOT BE NULL");
        }
        if (password == null) {
            throw new IllegalArgumentException("THE PASSWORD MUST NOT BE NULL");
        }
        if (salt == null) {
            salt = "";
        }
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(password.getBytes());
            md.update(salt.getBytes());
            result = SecurityHelper.byteArrayToHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new DruidSafeRuntimeException(e);
        }
        return result;
    }
