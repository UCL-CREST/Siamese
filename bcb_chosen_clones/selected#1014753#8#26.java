    public static String getMD5(String... list) {
        if (list.length == 0) return null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.reset();
        for (String in : list) md.update(in.getBytes());
        byte[] digest = md.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
