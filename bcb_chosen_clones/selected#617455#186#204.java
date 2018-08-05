    private String getMD5(String data) {
        try {
            MessageDigest md5Algorithm = MessageDigest.getInstance("MD5");
            md5Algorithm.update(data.getBytes(), 0, data.length());
            byte[] digest = md5Algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            String hexDigit = null;
            for (int i = 0; i < digest.length; i++) {
                hexDigit = Integer.toHexString(0xFF & digest[i]);
                if (hexDigit.length() < 2) {
                    hexDigit = "0" + hexDigit;
                }
                hexString.append(hexDigit);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ne) {
            return data;
        }
    }
