    private String md5(String s) {
        StringBuffer hexString = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String hashPart = Integer.toHexString(0xFF & messageDigest[i]);
                if (hashPart.length() == 1) {
                    hashPart = "0" + hashPart;
                }
                hexString.append(hashPart);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(this.getClass().getSimpleName(), "MD5 algorithm not present");
        }
        return hexString != null ? hexString.toString() : null;
    }
