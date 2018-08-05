    private void getRandomGUID(boolean secure) {
        MessageDigest md5;
        StringBuilder sbValueBeforeHash = new StringBuilder();
        try {
            md5 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new ApplicationIllegalArgumentException(e);
        }
        long time = System.nanoTime();
        long rand = 0;
        if (secure) {
            rand = MySecureRand.nextLong();
        } else {
            rand = MyRand.nextLong();
        }
        sbValueBeforeHash.append(SId);
        sbValueBeforeHash.append(":");
        sbValueBeforeHash.append(Long.toString(time));
        sbValueBeforeHash.append(":");
        sbValueBeforeHash.append(Long.toString(rand));
        valueBeforeHash = sbValueBeforeHash.toString();
        md5.update(valueBeforeHash.getBytes());
        byte[] array = md5.digest();
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < array.length; ++j) {
            int b = array[j] & 0xFF;
            if (b < 0x10) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(b));
        }
        valueAfterHash = sb.toString();
    }
