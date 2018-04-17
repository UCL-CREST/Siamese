    public static String md5Hash(String inString) throws TopicSpacesException {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(inString.getBytes());
            byte[] array = md5.digest();
            StringBuffer buf = new StringBuffer();
            int len = array.length;
            for (int i = 0; i < len; i++) {
                int b = array[i] & 0xFF;
                buf.append(Integer.toHexString(b));
            }
            return buf.toString();
        } catch (Exception x) {
            throw new TopicSpacesException(x);
        }
    }
