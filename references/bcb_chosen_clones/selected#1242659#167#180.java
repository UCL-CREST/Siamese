    static String getMD5Hash(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte[] b = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            int v = (int) b[i];
            v = v < 0 ? 0x100 + v : v;
            String cc = Integer.toHexString(v);
            if (cc.length() == 1) sb.append('0');
            sb.append(cc);
        }
        return sb.toString();
    }
