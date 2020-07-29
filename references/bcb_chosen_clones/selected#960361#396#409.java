    private String getEncryptedPassword() {
        String encrypted;
        char[] pwd = password.getPassword();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(new String(pwd).getBytes("UTF-8"));
            byte[] digested = md.digest();
            encrypted = new String(Base64Coder.encode(digested));
        } catch (Exception e) {
            encrypted = new String(pwd);
        }
        for (int i = 0; i < pwd.length; i++) pwd[i] = 0;
        return encrypted;
    }
