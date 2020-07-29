    public synchronized String encrypt(String plaintext) {
        if (plaintext == null) plaintext = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        try {
            md.update(plaintext.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        byte raw[] = md.digest();
        String hash = "";
        try {
            hash = Base64Encoder.encode(raw);
        } catch (IOException e1) {
            System.err.println("Error encoding password using Jboss Base64Encoder");
            e1.printStackTrace();
        }
        return hash;
    }
