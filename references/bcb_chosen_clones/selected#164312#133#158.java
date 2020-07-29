    public String encrypt(String text, String passphrase, int keylen) {
        RC2ParameterSpec parm = new RC2ParameterSpec(keylen);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(passphrase.getBytes(getCharset()));
            SecretKeySpec skeySpec = new SecretKeySpec(md.digest(), "RC2");
            Cipher cipher = Cipher.getInstance("RC2/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, parm);
            byte[] newBytes = encodeStringNew(text);
            byte[] d = cipher.doFinal(newBytes);
            return Base64.encodeBytes(d);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
