    public String getDigest(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance(hashName);
        md.update(s.getBytes());
        byte[] dig = md.digest();
        return Base16.toHexString(dig);
    }
