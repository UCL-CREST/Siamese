    public void init(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes("UTF-8"), 0, password.length());
            byte[] rawKey = md.digest();
            skeySpec = new SecretKeySpec(rawKey, "AES");
            ivSpec = new IvParameterSpec(rawKey);
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
