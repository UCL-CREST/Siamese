    public static String getHashedPasswordTc(String password) throws java.security.NoSuchAlgorithmException {
        java.security.MessageDigest d = java.security.MessageDigest.getInstance("MD5");
        d.reset();
        d.update(password.getBytes());
        byte[] buf = d.digest();
        char[] cbf = new char[buf.length * 2];
        for (int jj = 0, kk = 0; jj < buf.length; jj++) {
            cbf[kk++] = "0123456789abcdef".charAt((buf[jj] >> 4) & 0x0F);
            cbf[kk++] = "0123456789abcdef".charAt(buf[jj] & 0x0F);
        }
        return new String(cbf);
    }
