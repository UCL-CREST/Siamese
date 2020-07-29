    public void setContentMD5() {
        MessageDigest messagedigest = null;
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            contentMD5 = null;
        }
        messagedigest.update(content.getBytes());
        byte digest[] = messagedigest.digest();
        String chk = "";
        for (int i = 0; i < digest.length; i++) {
            String s = Integer.toHexString(digest[i] & 0xFF);
            chk += ((s.length() == 1) ? "0" + s : s);
        }
        contentMD5 = chk;
    }
