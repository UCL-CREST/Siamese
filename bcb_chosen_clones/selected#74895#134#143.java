    public void process(String t) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(MD5_DIGEST);
            md5.reset();
            md5.update(t.getBytes());
            callback.display(null, digestToHexString(md5.digest()));
        } catch (Exception ex) {
            callback.display(null, "[failed]");
        }
    }
