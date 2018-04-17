    public static String md5(String word) {
        MessageDigest alg = null;
        try {
            alg = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ServletUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        alg.reset();
        alg.update(word.getBytes());
        byte[] digest = alg.digest();
        StringBuilder hashedWord = new StringBuilder();
        String hx;
        for (int i = 0; i < digest.length; i++) {
            hx = Integer.toHexString(0xFF & digest[i]);
            if (hx.length() == 1) {
                hx = "0" + hx;
            }
            hashedWord.append(hx);
        }
        return hashedWord.toString();
    }
