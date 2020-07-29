    public static String hashPassword(String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            byte result[] = md5.digest("InTeRlOgY".getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                String s = Integer.toHexString(result[i]);
                int length = s.length();
                if (length >= 2) {
                    sb.append(s.substring(length - 2, length));
                } else {
                    sb.append("0");
                    sb.append(s);
                }
            }
            return "{md5}" + sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }
