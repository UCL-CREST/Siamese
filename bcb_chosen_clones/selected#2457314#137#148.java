    protected String encrypt(String text) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
            md.update(text.getBytes("UTF-8"));
            byte raw[] = md.digest();
            String hash = (new BASE64Encoder()).encode(raw);
            return hash;
        } catch (Exception ex) {
            throw new TiiraException(ex);
        }
    }
