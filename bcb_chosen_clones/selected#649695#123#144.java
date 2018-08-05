    public static String getSHA1(String data) throws NoSuchAlgorithmException {
        String addr;
        data = data.toLowerCase(Locale.getDefault());
        if (data.startsWith("mailto:")) {
            addr = data.substring(7);
        } else {
            addr = data;
        }
        MessageDigest md = MessageDigest.getInstance("SHA");
        StringBuffer sb = new StringBuffer();
        md.update(addr.getBytes());
        byte[] digest = md.digest();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(digest[i]);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            hex = hex.substring(hex.length() - 2);
            sb.append(hex);
        }
        return sb.toString();
    }
