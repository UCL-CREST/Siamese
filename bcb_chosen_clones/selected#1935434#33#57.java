    public InputStream encrypt(InputStream in, SecretKey key) throws InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        Cipher cipher;
        try {
            cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            CipherInputStream cis = new CipherInputStream(in, cipher);
            byte[] b = new byte[1024];
            int i = cis.read(b);
            while (i != -1) {
                fos.write(b, 0, i);
                i = cis.read(b);
            }
            cis.close();
            in.close();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CryptoUtil.outToInputStream(fos);
    }
