    public static String md5(String str) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            String md5 = hexString.toString();
            Log.v(FileUtil.class.getName(), md5);
            return md5;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
