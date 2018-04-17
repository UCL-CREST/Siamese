    private String mkSid() {
        String temp = toString();
        MessageDigest messagedigest = null;
        try {
            messagedigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        messagedigest.update(temp.getBytes());
        byte digest[] = messagedigest.digest();
        String chk = "";
        for (int i = 0; i < digest.length; i++) {
            String s = Integer.toHexString(digest[i] & 0xFF);
            chk += ((s.length() == 1) ? "0" + s : s);
        }
        return chk.toString();
    }
