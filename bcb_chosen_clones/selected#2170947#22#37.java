    public static byte[] encrypt(String passphrase, byte[] data) throws Exception {
        byte[] dataTemp;
        try {
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passphrase.getBytes());
            DESKeySpec key = new DESKeySpec(md.digest());
            SecretKeySpec DESKey = new SecretKeySpec(key.getKey(), "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, DESKey);
            dataTemp = cipher.doFinal(data);
        } catch (Exception e) {
            throw e;
        }
        return dataTemp;
    }
