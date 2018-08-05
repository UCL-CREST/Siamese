    public static synchronized String toSHA1(String str) {
        Nulls.failIfNull(str, "Cannot create an SHA1 encryption form a NULL string");
        try {
            MessageDigest md;
            md = MessageDigest.getInstance(SHA1);
            byte[] sha1hash = new byte[40];
            md.update(str.getBytes(ISO_CHARSET), 0, str.length());
            sha1hash = md.digest();
            return convertToHex(sha1hash);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return null;
    }
