    public static String md5sum(String s, String alg) {
        try {
            MessageDigest md = MessageDigest.getInstance(alg);
            md.update(s.getBytes(), 0, s.length());
            StringBuffer sb = new StringBuffer();
            synchronized (sb) {
                for (byte b : md.digest()) sb.append(pad(Integer.toHexString(0xFF & b), ZERO.charAt(0), 2, true));
            }
            return sb.toString();
        } catch (Exception ex) {
            log(ex);
        }
        return null;
    }
