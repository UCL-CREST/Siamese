    private StringBuffer hashPassword(StringBuffer password, String mode) {
        MessageDigest m = null;
        StringBuffer hash = new StringBuffer();
        try {
            m = MessageDigest.getInstance(mode);
            m.update(password.toString().getBytes("UTF8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] digest = m.digest();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(digest[i]);
            if (hex.length() == 1) hex = "0" + hex;
            hex = hex.substring(hex.length() - 2);
            hash.append(hex);
        }
        return hash;
    }
