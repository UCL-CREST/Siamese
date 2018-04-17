    public static String convetToSignature(Map<String, String> keyVal, String apiSecret) {
        if (keyVal == null || apiSecret == null || keyVal.size() <= 0 || apiSecret.trim().equals("")) {
            throw new IllegalArgumentException("keyVal or api secret is not valid. Please Check it again.");
        }
        Iterator<Entry<String, String>> iterator = keyVal.entrySet().iterator();
        StringBuffer rslt = new StringBuffer();
        byte[] signature = null;
        while (iterator.hasNext()) {
            Entry<String, String> entry = iterator.next();
            rslt.append(entry.getKey());
            rslt.append("=");
            rslt.append(entry.getValue());
        }
        rslt.append(apiSecret);
        try {
            MessageDigest md5 = MessageDigest.getInstance(HASHING_METHOD);
            md5.reset();
            md5.update(rslt.toString().getBytes());
            rslt.delete(0, rslt.length());
            signature = md5.digest();
            for (int i = 0; i < signature.length; i++) {
                String hex = Integer.toHexString(0xff & signature[i]);
                if (hex.length() == 1) {
                    rslt.append('0');
                }
                rslt.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return rslt.toString();
    }
