    private String hashMD5(String strToHash) throws Exception {
        try {
            byte[] bHash = new byte[strToHash.length() * 2];
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strToHash.getBytes("UTF-16LE"));
            bHash = md.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte element : bHash) {
                String strTemp = Integer.toHexString(element);
                hexString.append(strTemp.replaceAll("f", ""));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException duu) {
            throw new Exception("NoSuchAlgorithmException: " + duu.getMessage());
        }
    }
