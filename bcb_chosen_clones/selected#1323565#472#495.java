    private byte[] encriptData(byte[] raw, PublicKey key) {
        Cipher ciper = null;
        byte[] encripted = null;
        try {
            ciper = Cipher.getInstance("RSA");
            ciper.init(Cipher.ENCRYPT_MODE, key);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            out.write(ciper.doFinal(raw));
            encripted = out.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(GroupManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(GroupManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(GroupManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(GroupManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GroupManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(GroupManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encripted;
    }
