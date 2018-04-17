    public static String eventHash(String eventstr) {
        try {
            if (md == null) {
                md = MessageDigest.getInstance("MD5");
            }
            md.update(eventstr.getBytes("utf-8"));
            byte[] theDigest = md.digest();
            return new BASE64Encoder().encode(theDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
