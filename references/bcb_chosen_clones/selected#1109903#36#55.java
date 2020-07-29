    public static String generateHash(String key) {
        key += "use_your_key_here";
        MessageDigest md;
        try {
            md = java.security.MessageDigest.getInstance("MD5");
            md.reset();
            md.update(key.getBytes());
            byte[] bytes = md.digest();
            StringBuffer buff = new StringBuffer();
            for (int l = 0; l < bytes.length; l++) {
                String hx = Integer.toHexString(0xFF & bytes[l]);
                if (hx.length() == 1) buff.append("0");
                buff.append(hx);
            }
            return buff.toString().trim();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
