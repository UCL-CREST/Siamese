    public static String md5(String texto) {
        String resultado;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(texto.getBytes());
            BigInteger hash = new BigInteger(1, md5.digest());
            resultado = hash.toString(16);
            if (resultado.length() < 32) {
                char chars[] = new char[32 - resultado.length()];
                Arrays.fill(chars, '0');
                resultado = new String(chars) + resultado;
            }
        } catch (NoSuchAlgorithmException e) {
            resultado = e.toString();
        }
        return resultado;
    }
