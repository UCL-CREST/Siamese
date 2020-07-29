    private static String digest(String myinfo) {
        try {
            MessageDigest alga = MessageDigest.getInstance("SHA");
            alga.update(myinfo.getBytes());
            byte[] digesta = alga.digest();
            return byte2hex(digesta);
        } catch (Exception ex) {
            return myinfo;
        }
    }
