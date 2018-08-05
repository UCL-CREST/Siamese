    public synchronized String encrypt(String plainText) {
        String hash = null;
        try {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                throw new NoSuchAlgorithmException();
            }
            try {
                if (plainText != null) md.update(plainText.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new UnsupportedEncodingException();
            }
            byte raw[] = md.digest();
            hash = (new BASE64Encoder()).encode(raw);
        } catch (NoSuchAlgorithmException e) {
            MessageLog.writeErrorMessage(e, this);
        } catch (UnsupportedEncodingException e) {
            MessageLog.writeErrorMessage(e, this);
        }
        return Util.stripChars(hash);
    }
