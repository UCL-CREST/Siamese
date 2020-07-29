    public static String getEncodedPassword(String buff) {
        if (buff == null) return null;
        String t = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buff.getBytes());
            byte[] r = md.digest();
            for (int i = 0; i < r.length; i++) {
                t += toHexString(r[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
