    public static String digest(String str) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            md5.update(str.getBytes("ISO8859-1"));
            byte[] array = md5.digest();
            for (int x = 0; x < 16; x++) {
                if ((array[x] & 0xff) < 0x10) sb.append("0");
                sb.append(Long.toString(array[x] & 0xff, 16));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return sb.toString();
    }
