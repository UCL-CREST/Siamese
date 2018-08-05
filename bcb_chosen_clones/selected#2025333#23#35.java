    @SuppressWarnings(value = "RetentionPolicy.SOURCE")
    public static byte[] getHashMD5(String chave) {
        byte[] hashMd5 = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(chave.getBytes());
            hashMd5 = md.digest();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            Dialog.erro(ex.getMessage(), null);
        }
        return (hashMd5);
    }
