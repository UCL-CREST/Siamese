    public static String md5(String input) {
        byte[] temp;
        try {
            MessageDigest messageDigest;
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes());
            temp = messageDigest.digest();
        } catch (Exception e) {
            return null;
        }
        return MyUtils.byte2HexStr(temp);
    }
