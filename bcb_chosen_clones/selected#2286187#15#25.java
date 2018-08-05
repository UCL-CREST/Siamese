    public static String encode(String username, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(username.getBytes());
            digest.update(password.getBytes());
            return new String(digest.digest());
        } catch (Exception e) {
            Log.error("Error encrypting credentials", e);
        }
        return null;
    }
