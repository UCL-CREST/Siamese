    public static String compute(String text) {
        try {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            byte[] md5hash = new byte[32];
            md.update(text.getBytes("UTF-8"), 0, text.length());
            md5hash = md.digest();
            return convertToHex(md5hash);
        } catch (NoSuchAlgorithmException nax) {
            RuntimeException rx = new IllegalStateException();
            rx.initCause(rx);
            throw rx;
        } catch (UnsupportedEncodingException uex) {
            RuntimeException rx = new IllegalStateException();
            rx.initCause(uex);
            throw rx;
        }
    }
