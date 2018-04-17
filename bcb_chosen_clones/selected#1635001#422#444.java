    public static String MD5(String plainTxt) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainTxt.getBytes());
            byte[] b = md.digest();
            int i = 0;
            StringBuffer buffer = new StringBuffer();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buffer.append("0");
                }
                buffer.append(Integer.toHexString(i));
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
