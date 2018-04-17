    public String getmd5(String password) {
        String pwHash = "";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(password.getBytes());
            byte[] b = md.digest();
            for (int i = 0; i < b.length; i++) {
                pwHash += Integer.toString((b[i] & 0xFF) + 0x100, 16).substring(1);
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.fatal("MD5 Hash Algorithm not found", ex);
        }
        Logger.info("PWHash erzeugt und wird übergeben");
        return pwHash;
    }
