    private static String encodeMd5(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(key.getBytes());
            byte[] bytes = md.digest();
            String result = toHexString(bytes);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
