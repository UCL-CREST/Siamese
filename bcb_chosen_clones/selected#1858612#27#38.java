    public static String genDigest(String info) {
        MessageDigest alga;
        byte[] digesta = null;
        try {
            alga = MessageDigest.getInstance("SHA-1");
            alga.update(info.getBytes());
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byte2hex(digesta);
    }
