    public static String encryptPassword(String password) {
        String hash = null;
        try {
            MessageDigest md = null;
            md = MessageDigest.getInstance("SHA");
            md.update(password.getBytes("UTF-8"));
            byte raw[] = md.digest();
            hash = Base64.encode(raw, false);
        } catch (Exception e) {
        }
        return hash;
    }
