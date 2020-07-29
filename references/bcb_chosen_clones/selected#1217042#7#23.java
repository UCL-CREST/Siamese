    public static String encryptPassword(String plainPassword) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(plainPassword.getBytes());
            byte[] digestBytes = messageDigest.digest();
            String hex = null;
            for (int i = 0; i < digestBytes.length; i++) {
                hex = Integer.toHexString(0xFF & digestBytes[i]);
                if (hex.length() < 2) sb.append("0");
                sb.append(hex);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return new String(sb);
    }
