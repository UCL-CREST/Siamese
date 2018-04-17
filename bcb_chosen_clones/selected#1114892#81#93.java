    public String hasheMotDePasse(String mdp) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
        }
        sha.reset();
        sha.update(mdp.getBytes());
        byte[] digest = sha.digest();
        String pass = new String(Base64.encode(digest));
        pass = "{SHA}" + pass;
        return pass;
    }
