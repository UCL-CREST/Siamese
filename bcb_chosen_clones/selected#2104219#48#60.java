    public static String SHA1(String text) {
        byte[] sha1hash = new byte[40];
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            sha1hash = md.digest();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return convertToHex(sha1hash);
    }
