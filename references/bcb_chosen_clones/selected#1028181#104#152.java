    public String digestResponse() {
        String digest = null;
        if (null == nonce) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(username.getBytes());
            md.update(":".getBytes());
            md.update(realm.getBytes());
            md.update(":".getBytes());
            md.update(password.getBytes());
            byte[] d = md.digest();
            if (null != algorithm && -1 != (algorithm.toLowerCase()).indexOf("md5-sess")) {
                md = MessageDigest.getInstance("MD5");
                md.update(d);
                md.update(":".getBytes());
                md.update(nonce.getBytes());
                md.update(":".getBytes());
                md.update(cnonce.getBytes());
                d = md.digest();
            }
            byte[] a1 = bytesToHex(d);
            md = MessageDigest.getInstance("MD5");
            md.update(method.getBytes());
            md.update(":".getBytes());
            md.update(uri.getBytes());
            d = md.digest();
            byte[] a2 = bytesToHex(d);
            md = MessageDigest.getInstance("MD5");
            md.update(a1);
            md.update(":".getBytes());
            md.update(nonce.getBytes());
            md.update(":".getBytes());
            if (null != qop) {
                md.update(nonceCount.getBytes());
                md.update(":".getBytes());
                md.update(cnonce.getBytes());
                md.update(":".getBytes());
                md.update(qop.getBytes());
                md.update(":".getBytes());
            }
            md.update(a2);
            d = md.digest();
            byte[] r = bytesToHex(d);
            digest = new String(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digest;
    }
