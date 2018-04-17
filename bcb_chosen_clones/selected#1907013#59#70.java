    public static String encode(String plaintext) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(plaintext.getBytes("UTF-8"));
            byte raw[] = md.digest();
            String hash = (new BASE64Encoder()).encode(raw);
            return hash;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not encrypt password for ISA db verification");
        }
    }
