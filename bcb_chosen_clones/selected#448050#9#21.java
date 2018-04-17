    public static String encriptar(String string) throws Exception {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new Exception("Algoritmo de Criptografia não encontrado.");
        }
        md.update(string.getBytes());
        BigInteger hash = new BigInteger(1, md.digest());
        String retorno = hash.toString(16);
        return retorno;
    }
