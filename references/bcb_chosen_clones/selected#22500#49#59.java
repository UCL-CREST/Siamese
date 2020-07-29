    public static String md5(String text) {
        String hashed = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(text.getBytes(), 0, text.length());
            hashed = new BigInteger(1, digest.digest()).toString(16);
        } catch (Exception e) {
            Log.e(ctGlobal.tag, "ctCommon.md5: " + e.toString());
        }
        return hashed;
    }
