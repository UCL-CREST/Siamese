    protected String getPasswordHash(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 algorithm not found", e);
            throw new ServiceException(e);
        }
        md.update(password.getBytes());
        byte[] hash = md.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            buf.append(Integer.toHexString(hash[i] & 0xff));
        }
        return buf.toString();
    }
