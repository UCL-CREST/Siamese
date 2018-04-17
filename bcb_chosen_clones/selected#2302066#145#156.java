    public static String encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(input.getBytes("UTF-8"));
            return toHexString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
