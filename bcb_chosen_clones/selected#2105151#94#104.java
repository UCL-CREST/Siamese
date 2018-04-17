    private String getPrefsKey(String key) {
        try {
            if (MD5 == null) MD5 = MessageDigest.getInstance("MD5");
            MD5.reset();
            MD5.update(key.getBytes("UTF-8"));
            byte[] resultBytes = MD5.digest();
            return toHexString(resultBytes);
        } catch (Exception nsae) {
            return key;
        }
    }
