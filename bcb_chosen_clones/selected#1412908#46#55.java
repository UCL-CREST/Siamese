    public String crypt(String suppliedPassword) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(suppliedPassword.getBytes());
        String encriptedPassword = null;
        try {
            encriptedPassword = new String(Base64.encode(md.digest()), "ASCII");
        } catch (UnsupportedEncodingException e) {
        }
        return encriptedPassword;
    }
