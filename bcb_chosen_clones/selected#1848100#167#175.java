    private String hashPassword(String password) throws NoSuchAlgorithmException {
        String hash = null;
        MessageDigest md = MessageDigest.getInstance("SHA");
        log.debug("secure hash on password " + password);
        md.update(password.getBytes());
        hash = new String(Base64.encodeBase64(md.digest()));
        log.debug("returning hash " + hash);
        return hash;
    }
