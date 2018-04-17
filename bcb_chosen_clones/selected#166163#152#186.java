    public String getRandomGUID() {
        MessageDigest md5 = null;
        String valueBeforeMD5 = "";
        String retValue = "";
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error: " + e);
        }
        StringBuffer sbValueBeforeMD5 = new StringBuffer();
        try {
            InetAddress id = InetAddress.getLocalHost();
            long time = System.currentTimeMillis();
            long rand = 0;
            rand = random.nextLong();
            sbValueBeforeMD5.append(id.toString());
            sbValueBeforeMD5.append(Long.toString(time));
            sbValueBeforeMD5.append(Long.toString(rand));
            valueBeforeMD5 = sbValueBeforeMD5.toString();
            md5.update(valueBeforeMD5.getBytes());
            byte[] array = md5.digest();
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < array.length; ++j) {
                int b = array[j] & 0xFF;
                if (b < 0x10) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(b));
            }
            retValue = sb.toString();
        } catch (UnknownHostException e) {
            System.out.println("Error:" + e);
        }
        return retValue;
    }
