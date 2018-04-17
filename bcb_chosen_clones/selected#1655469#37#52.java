    public synchronized String encrypt(String password) {
        try {
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA-1");
            md.update(password.getBytes("UTF-8"));
            byte raw[] = md.digest();
            byte[] hash = (new Base64()).encode(raw);
            return new String(hash);
        } catch (NoSuchAlgorithmException e) {
            logger.error("Algorithm SHA-1 is not supported", e.getCause());
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error("UTF-8 encoding is not supported");
            return null;
        }
    }
