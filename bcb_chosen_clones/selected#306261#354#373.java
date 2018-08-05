    public static String md5(String str) {
        StringBuffer buf = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] data = new byte[32];
            md.update(str.getBytes(md5Encoding), 0, str.length());
            data = md.digest();
            for (int i = 0; i < data.length; i++) {
                int halfbyte = (data[i] >>> 4) & 0x0F;
                int two_halfs = 0;
                do {
                    if ((0 <= halfbyte) && (halfbyte <= 9)) buf.append((char) ('0' + halfbyte)); else buf.append((char) ('a' + (halfbyte - 10)));
                    halfbyte = data[i] & 0x0F;
                } while (two_halfs++ < 1);
            }
        } catch (Exception e) {
            errorLog("{Malgn.md5} " + e.getMessage());
        }
        return buf.toString();
    }
