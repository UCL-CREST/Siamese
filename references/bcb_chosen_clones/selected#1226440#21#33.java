    public static String encodePassword(String plainTextPassword) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        StringBuffer sb = new StringBuffer();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(plainTextPassword.getBytes("UTF-8"));
        byte[] digestBytes = messageDigest.digest();
        String hex = null;
        for (int i = 0; i < digestBytes.length; i++) {
            hex = Integer.toHexString(0xFF & digestBytes[i]);
            if (hex.length() < 2) sb.append("0");
            sb.append(hex);
        }
        return new String(sb);
    }
