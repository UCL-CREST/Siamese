    public synchronized String encryptPassword(String passwordString) throws Exception {
        MessageDigest digest = null;
        digest = MessageDigest.getInstance("SHA");
        digest.update(passwordString.getBytes("UTF-8"));
        byte raw[] = digest.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }
