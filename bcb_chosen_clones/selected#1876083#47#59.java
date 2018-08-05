    public String obfuscateString(String string) {
        String obfuscatedString = null;
        try {
            MessageDigest md = MessageDigest.getInstance(ENCRYPTION_ALGORITHM);
            md.update(string.getBytes());
            byte[] digest = md.digest();
            obfuscatedString = new String(Base64.encode(digest)).replace(DELIM_PATH, '=');
        } catch (NoSuchAlgorithmException e) {
            StatusHandler.log("SHA not available", null);
            obfuscatedString = LABEL_FAILED_TO_OBFUSCATE;
        }
        return obfuscatedString;
    }
