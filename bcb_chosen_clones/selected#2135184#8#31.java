    public static String EncryptString(String method, String input) {
        MessageDigest md = null;
        byte[] byteHash = null;
        StringBuffer resultString = new StringBuffer();
        if (method.equals("SHA1") || method.equals("MD5")) {
            try {
                md = MessageDigest.getInstance(method);
            } catch (NoSuchAlgorithmException e) {
                System.out.println("NoSuchAlgorithmException caught!");
                return null;
            }
        } else {
            return null;
        }
        md.reset();
        md.update(input.getBytes());
        byteHash = md.digest();
        for (int i = 0; i < byteHash.length; i++) {
            String tmp = Integer.toHexString(0xff & byteHash[i]);
            if (tmp.length() < 2) tmp = "0" + tmp;
            resultString.append(tmp);
        }
        return (resultString.toString());
    }
