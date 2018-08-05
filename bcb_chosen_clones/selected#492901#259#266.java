    private String endcodePassword(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(password.getBytes("UTF-8"));
        byte raw[] = md.digest();
        Base64 base64 = new Base64();
        String hash = new String(base64.encode(raw));
        return hash;
    }
