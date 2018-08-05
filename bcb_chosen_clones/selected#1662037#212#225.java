    public static String criptografar(String senha) {
        if (senha == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(senha.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(digest.digest());
        } catch (NoSuchAlgorithmException ns) {
            LoggerFactory.getLogger(UtilAdrs.class).error(Msg.EXCEPTION_MESSAGE, UtilAdrs.class.getSimpleName(), ns);
            return senha;
        }
    }
