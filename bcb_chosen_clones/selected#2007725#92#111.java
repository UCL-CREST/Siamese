    public static final String hash(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            password = password + salt;
            md.update(password.getBytes("utf8"));
            byte[] b = md.digest();
            StringBuilder output = new StringBuilder(32);
            for (int i = 0; i < b.length; i++) {
                String temp = Integer.toHexString(b[i] & 0xff);
                if (temp.length() < 2) {
                    output.append("0");
                }
                output.append(temp);
            }
            return output.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
