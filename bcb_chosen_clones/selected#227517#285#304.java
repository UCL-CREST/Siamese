    private static String getSignature(String data) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "FFFFFFFFFFFFFFFF";
        }
        md.update(data.getBytes());
        StringBuffer sb = new StringBuffer();
        byte[] sign = md.digest();
        for (int i = 0; i < sign.length; i++) {
            byte b = sign[i];
            int in = (int) b;
            if (in < 0) in = 127 - b;
            String hex = Integer.toHexString(in).toUpperCase();
            if (hex.length() == 1) hex = "0" + hex;
            sb.append(hex);
        }
        return sb.toString();
    }
