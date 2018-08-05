    public String setEncryptedPassword(String rawPassword) {
        String out = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(rawPassword.getBytes());
            byte raw[] = md.digest();
            out = new String();
            for (int x = 0; x < raw.length; x++) {
                String hex2 = Integer.toHexString((int) raw[x] & 0xFF);
                if (1 == hex2.length()) {
                    hex2 = "0" + hex2;
                }
                out += hex2;
                int a = 1;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return out;
    }
