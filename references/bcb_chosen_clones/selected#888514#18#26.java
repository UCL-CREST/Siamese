    private static byte[] gerarHash(String frase) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(frase.getBytes());
            return md.digest();
        } catch (Exception e) {
            return null;
        }
    }
