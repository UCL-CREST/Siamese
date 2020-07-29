    public synchronized String getEncryptedPassword(String plaintext, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
            md.update(plaintext.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException nsae) {
            throw nsae;
        } catch (UnsupportedEncodingException uee) {
            throw uee;
        }
        return (new BigInteger(1, md.digest())).toString(16);
    }
