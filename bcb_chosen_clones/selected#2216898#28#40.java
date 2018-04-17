    public static String encodePassword(String password) {
        try {
            MessageDigest messageDiegest = MessageDigest.getInstance("SHA-1");
            messageDiegest.update(password.getBytes("UTF-8"));
            return Base64.encodeToString(messageDiegest.digest(), false);
        } catch (NoSuchAlgorithmException e) {
            log.error("Exception while encoding password");
            throw new Error(e);
        } catch (UnsupportedEncodingException e) {
            log.error("Exception while encoding password");
            throw new Error(e);
        }
    }
