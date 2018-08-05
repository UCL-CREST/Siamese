    public static String makeMD5(String input) throws Exception {
        String dstr = null;
        byte[] digest;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            digest = md.digest();
            dstr = new BigInteger(1, digest).toString(16);
            if (dstr.length() % 2 > 0) {
                dstr = "0" + dstr;
            }
        } catch (Exception e) {
            throw new Exception("Erro inesperado em makeMD5(): " + e.toString(), e);
        }
        return dstr;
    }
