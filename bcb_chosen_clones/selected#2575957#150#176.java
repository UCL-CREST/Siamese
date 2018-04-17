    public static String generateMessageId(String plain) {
        byte[] cipher = new byte[35];
        String messageId = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(plain.getBytes());
            cipher = md5.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < cipher.length; i++) {
                String hex = Integer.toHexString(0xff & cipher[i]);
                if (hex.length() == 1) sb.append('0');
                sb.append(hex);
            }
            StringBuffer pass = new StringBuffer();
            pass.append(sb.substring(0, 6));
            pass.append("H");
            pass.append(sb.substring(6, 11));
            pass.append("H");
            pass.append(sb.substring(11, 21));
            pass.append("H");
            pass.append(sb.substring(21));
            messageId = new String(pass);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return messageId;
    }
