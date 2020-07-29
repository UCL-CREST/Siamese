    private void getRandomGUID(boolean secure) {
        MessageDigest md5 = null;
        final StringBuilder sbValueBeforeMD5 = new StringBuilder();
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.fatal("", e);
            return;
        }
        try {
            final long time = System.currentTimeMillis();
            long rand = 0;
            if (secure) {
                rand = mySecureRand.nextLong();
            } else {
                rand = myRand.nextLong();
            }
            sbValueBeforeMD5.append(sId);
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(time));
            sbValueBeforeMD5.append(":");
            sbValueBeforeMD5.append(Long.toString(rand));
            valueBeforeMD5 = sbValueBeforeMD5.toString();
            md5.update(valueBeforeMD5.getBytes());
            final byte[] array = md5.digest();
            final StringBuilder sb = new StringBuilder();
            for (int j = 0; j < array.length; ++j) {
                final int b = array[j] & 0xFF;
                if (b < 0x10) {
                    sb.append('0');
                }
                sb.append(Integer.toHexString(b));
            }
            valueAfterMD5 = sb.toString();
        } catch (Exception e) {
            logger.fatal("", e);
        }
    }
