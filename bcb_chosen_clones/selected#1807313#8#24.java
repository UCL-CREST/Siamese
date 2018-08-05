    public static String md5(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(msg.getBytes());
            byte[] encodedPassword = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < encodedPassword.length; i++) {
                if ((encodedPassword[i] & 0xff) < 0x10) {
                    sb.append("0");
                }
                sb.append(Long.toString(encodedPassword[i] & 0xff, 16));
            }
            return new String(sb.toString());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
