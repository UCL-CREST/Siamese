    public String hash(String senha) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(senha.getBytes());
            byte[] hashMd5 = md.digest();
            for (int i = 0; i < hashMd5.length; i++) result += Integer.toHexString((((hashMd5[i] >> 4) & 0xf) << 4) | (hashMd5[i] & 0xf));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getInstancia().log(TipoLog.ERRO, ex);
        }
        return result;
    }
