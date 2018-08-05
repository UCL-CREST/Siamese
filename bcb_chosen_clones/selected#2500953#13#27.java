    public static String getSignature(String s) {
        try {
            final AsciiEncoder coder = new AsciiEncoder();
            final MessageDigest msgDigest = MessageDigest.getInstance("MD5");
            msgDigest.update(s.getBytes("UTF-8"));
            final byte[] digest = msgDigest.digest();
            return coder.encode(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }
