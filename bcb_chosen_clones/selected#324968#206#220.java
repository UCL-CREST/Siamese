    private static String hash(String string) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            return null;
        }
        try {
            md.update(string.getBytes("UTF-8"));
        } catch (Exception e) {
            return null;
        }
        byte raw[] = md.digest();
        return (new BASE64Encoder()).encode(raw);
    }
