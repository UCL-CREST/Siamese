    protected static String md5(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(s.getBytes());
        byte digest[] = md.digest();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            result.append(Integer.toHexString(0xFF & digest[i]));
        }
        return result.toString();
    }
