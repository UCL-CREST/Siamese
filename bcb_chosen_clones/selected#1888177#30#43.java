    public static String descripta(String senha) throws GCIException {
        LOGGER.debug(INICIANDO_METODO + "descripta(String)");
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(senha.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            LOGGER.fatal(e.getMessage(), e);
            throw new GCIException(e);
        } finally {
            LOGGER.debug(FINALIZANDO_METODO + "descripta(String)");
        }
    }
