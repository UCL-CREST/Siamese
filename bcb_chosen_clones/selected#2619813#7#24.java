    public static String hashString(String pwd) {
        StringBuffer hex = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pwd.getBytes());
            byte[] d = md.digest();
            String plaintxt;
            for (int i = 0; i < d.length; i++) {
                plaintxt = Integer.toHexString(0xFF & d[i]);
                if (plaintxt.length() < 2) {
                    plaintxt = "0" + plaintxt;
                }
                hex.append(plaintxt);
            }
        } catch (NoSuchAlgorithmException nsae) {
        }
        return hex.toString();
    }
