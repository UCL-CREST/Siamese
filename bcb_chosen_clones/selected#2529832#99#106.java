    public static String getMdPsw(String passwd) throws Exception {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(passwd.getBytes("iso-8859-1"), 0, passwd.length());
        md5hash = md.digest();
        return convertToHex(md5hash);
    }
