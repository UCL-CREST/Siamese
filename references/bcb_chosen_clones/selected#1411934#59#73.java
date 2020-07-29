    public static String getSSHADigest(String password, String salt) {
        String digest = null;
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-1");
            sha.reset();
            sha.update(password.getBytes());
            sha.update(salt.getBytes());
            byte[] pwhash = sha.digest();
            digest = "{SSHA}" + new String(Base64.encode(concatenate(pwhash, salt.getBytes())));
        } catch (NoSuchAlgorithmException nsae) {
            CofaxToolsUtil.log("Algorithme SHA-1 non supporte a la creation du hashage" + nsae + id);
        }
        return digest;
    }
