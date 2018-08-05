    public String hash(String s) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        md.update(s.getBytes());
        return Hex.toHex(md.digest());
    }
