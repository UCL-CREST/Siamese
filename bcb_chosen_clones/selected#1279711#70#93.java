    public InputStream encryptPBE(InputStream in, String password, PBEParameterSpec pbeparams) {
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory keyFac;
        try {
            keyFac = SecretKeyFactory.getInstance(pbe);
            SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
            Cipher pbeCipher = Cipher.getInstance(pbe);
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeparams);
            CipherInputStream cis = new CipherInputStream(in, pbeCipher);
            ByteArrayOutputStream fos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int i = cis.read(b);
            while (i != -1) {
                fos.write(b, 0, i);
                i = cis.read(b);
            }
            cis.close();
            in.close();
            return CryptoUtil.outToInputStream(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
