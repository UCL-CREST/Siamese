    public String encrypt(String retazec) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(retazec.getBytes());
        return HexString.bufferToHex(md.digest());
    }
