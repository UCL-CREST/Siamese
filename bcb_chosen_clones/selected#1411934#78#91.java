    public static String getSHADigest(String password) {
        String digest = null;
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
            sha.reset();
            sha.update(password.getBytes());
            byte[] pwhash = sha.digest();
            digest = "{SHA}" + new String(Base64.encode(pwhash));
        } catch (NoSuchAlgorithmException nsae) {
            CofaxToolsUtil.log("Algorithme SHA-1 non supporte a la creation du hashage" + nsae + id);
        }
        return digest;
    }
