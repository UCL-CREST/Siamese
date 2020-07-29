    public static String calcCRC(String phrase) {
        StringBuffer crcCalc = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(phrase.getBytes());
            byte[] tabDigest = md.digest();
            for (int i = 0; i < tabDigest.length; i++) {
                String octet = "0" + Integer.toHexString(tabDigest[i]);
                crcCalc.append(octet.substring(octet.length() - 2));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return crcCalc.toString();
    }
