    public static String encriptaSenha(String string) throws ApplicationException {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(digest.digest());
        } catch (NoSuchAlgorithmException ns) {
            ns.printStackTrace();
            throw new ApplicationException("Erro ao Encriptar Senha");
        }
    }
