    private String getCoded(String pass) {
        String passSecret = "";
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(pass.getBytes("UTF8"));
            byte s[] = m.digest();
            for (int i = 0; i < s.length; i++) {
                passSecret += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
            }
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return passSecret;
    }
