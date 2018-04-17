    public static void main(String args[]) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        KeyPair keyPair = generateKeyPair("RSA", 2048);
        saveToFile("public.key", keyPair.getPublic().getEncoded());
        saveToFile("private.key", keyPair.getPrivate().getEncoded());
    }
