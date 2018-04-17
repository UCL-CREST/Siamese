    public static String encipherAMessage(String message) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance(java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("SHA1"));
            sha1.update(message.getBytes(java.util.ResourceBundle.getBundle("com/jjcp/resources/Strings").getString("UTF-16LE")));
            byte[] digest = sha1.digest();
            BASE64Encoder base64encoder = new BASE64Encoder();
            String cipherTextB64 = base64encoder.encode(digest);
            return cipherTextB64;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SelectorView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SelectorView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }
