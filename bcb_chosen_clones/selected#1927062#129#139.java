    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest encript = MessageDigest.getInstance("MD5");
        encript.update(password.getBytes());
        byte[] b = encript.digest();
        int size = b.length;
        StringBuffer h = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            h.append(b[i]);
        }
        return h.toString();
    }
