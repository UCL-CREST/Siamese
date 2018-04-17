    public static byte[] gerarHash(String frase) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(frase.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
