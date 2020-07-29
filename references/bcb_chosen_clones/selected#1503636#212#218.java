    public boolean authenticate(String plaintext) throws NoSuchAlgorithmException {
        String[] passwordParts = this.password.split("\\$");
        md = MessageDigest.getInstance("SHA-1");
        md.update(passwordParts[1].getBytes());
        isAuthenticated = toHex(md.digest(plaintext.getBytes())).equalsIgnoreCase(passwordParts[2]);
        return isAuthenticated;
    }
