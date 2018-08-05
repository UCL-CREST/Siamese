    public synchronized String encrypt(String p_plainText) throws ServiceUnavailableException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceUnavailableException(e.getMessage());
        }
        try {
            md.update(p_plainText.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new ServiceUnavailableException(e.getMessage());
        }
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);
        return hash;
    }
