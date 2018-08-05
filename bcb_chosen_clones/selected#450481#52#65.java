    static String encodeEmailAsUserId(String email) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(email.toLowerCase().getBytes());
            StringBuilder builder = new StringBuilder();
            builder.append("1");
            for (byte b : md5.digest()) {
                builder.append(String.format("%02d", new Object[] { Integer.valueOf(b & 0xFF) }));
            }
            return builder.toString().substring(0, 20);
        } catch (NoSuchAlgorithmException ex) {
        }
        return "";
    }
