    public String getHash(String key, boolean base64) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(key.getBytes());
        if (base64) return new String(new Base64().encode(md.digest()), "UTF8"); else return new String(md.digest(), "UTF8");
    }
