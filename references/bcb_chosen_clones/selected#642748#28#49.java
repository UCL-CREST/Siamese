    public static String generateMD5(String clear) {
        byte hash[] = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(clear.getBytes());
            hash = md5.digest();
        } catch (NoSuchAlgorithmException e) {
        }
        if (hash != null) {
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hash.length; i++) {
                String tmp = Integer.toHexString(0xFF & hash[i]);
                if (tmp.length() == 1) {
                    tmp = "0" + tmp;
                }
                hexString.append(tmp);
            }
            return hexString.toString();
        } else {
            return null;
        }
    }
