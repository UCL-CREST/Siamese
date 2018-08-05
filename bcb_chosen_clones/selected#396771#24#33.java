    public String hash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance(hashFunction);
            md.update(text.getBytes(charset));
            byte[] raw = md.digest();
            return new String(encodeHex(raw));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
