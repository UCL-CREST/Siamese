    public RandomGUID() {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getClass().getName() + ": " + e.getMessage());
        }
        try {
            long time = System.currentTimeMillis();
            long rand = 0;
            rand = myRand.nextLong();
            StringBuffer sb = new StringBuffer();
            sb.append(s_id);
            sb.append(":");
            sb.append(Long.toString(time));
            sb.append(":");
            sb.append(Long.toString(rand));
            md5.update(sb.toString().getBytes());
            byte[] array = md5.digest();
            sb.setLength(0);
            for (int j = 0; j < array.length; ++j) {
                int b = array[j] & 0xFF;
                if (b < 0x10) sb.append('0');
                sb.append(Integer.toHexString(b));
            }
            valueAfterMD5 = sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getClass().getName() + ": " + e.getMessage());
        }
    }
