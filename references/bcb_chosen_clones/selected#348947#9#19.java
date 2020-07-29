    public static String cryptografar(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(senha.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            senha = hash.toString(16);
        } catch (NoSuchAlgorithmException ns) {
            ns.printStackTrace();
        }
        return senha;
    }
