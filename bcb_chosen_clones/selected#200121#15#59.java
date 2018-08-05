    public static String generateGuid(boolean secure) {
        MessageDigest md5 = null;
        String valueBeforeMD5 = null;
        String valueAfterMD5 = null;
        StringBuffer sbValueBeforeMD5 = new StringBuffer();
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: " + e);
        }
        try {
            long time = System.currentTimeMillis();
            long rand = 0L;
            if (secure) rand = mySecureRand.nextLong(); else rand = myRand.nextLong();
            sbValueBeforeMD5.append(s_id);
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(time));
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(rand));
            valueBeforeMD5 = sbValueBeforeMD5.toString();
            md5.update(valueBeforeMD5.getBytes());
            byte array[] = md5.digest();
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < array.length; j++) {
                int b = array[j] & 0xff;
                if (b < 16) sb.append('0');
                sb.append(Integer.toHexString(b));
            }
            valueAfterMD5 = sb.toString();
        } catch (Exception e) {
            System.out.println("Error:" + e);
        }
        String raw = valueAfterMD5.toUpperCase();
        StringBuffer sb = new StringBuffer();
        sb.append(raw.substring(0, 8));
        sb.append("-");
        sb.append(raw.substring(8, 12));
        sb.append("-");
        sb.append(raw.substring(12, 16));
        sb.append("-");
        sb.append(raw.substring(16, 20));
        sb.append("-");
        sb.append(raw.substring(20));
        return sb.toString();
    }
