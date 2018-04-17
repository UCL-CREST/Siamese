    public static String encodeMD5(String input) {
        MessageDigest md = null;
        byte[] byteHash = null;
        StringBuffer resultString = new StringBuffer();
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        }
        md.reset();
        md.update(input.getBytes());
        byteHash = md.digest();
        for (int i = 0; i < byteHash.length; i++) {
            resultString.append(Integer.toHexString(0xFF & byteHash[i]));
        }
        return (resultString.toString());
    }
