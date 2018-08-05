    private static String genRandomGUID(boolean secure) {
        String valueBeforeMD5 = "";
        String valueAfterMD5 = "";
        MessageDigest md5 = null;
        StringBuffer sbValueBeforeMD5 = new StringBuffer();
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: " + e);
            return valueBeforeMD5;
        }
        long time = System.currentTimeMillis();
        long rand = 0;
        if (secure) {
            rand = mySecureRand.nextLong();
        } else {
            rand = myRand.nextLong();
        }
        sbValueBeforeMD5.append(s_id);
        sbValueBeforeMD5.append(":");
        sbValueBeforeMD5.append(Long.toString(time));
        sbValueBeforeMD5.append(":");
        sbValueBeforeMD5.append(Long.toString(rand));
        valueBeforeMD5 = sbValueBeforeMD5.toString();
        md5.update(valueBeforeMD5.getBytes());
        byte[] array = md5.digest();
        String strTemp = "";
        for (int i = 0; i < array.length; i++) {
            strTemp = (Integer.toHexString(array[i] & 0XFF));
            if (strTemp.length() == 1) {
                valueAfterMD5 = valueAfterMD5 + "0" + strTemp;
            } else {
                valueAfterMD5 = valueAfterMD5 + strTemp;
            }
        }
        return valueAfterMD5.toUpperCase();
    }
