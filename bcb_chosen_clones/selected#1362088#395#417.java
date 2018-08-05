    public static String encrypt(String plainText) {
        if (TextUtils.isEmpty(plainText)) {
            plainText = "";
        }
        StringBuilder text = new StringBuilder();
        for (int i = plainText.length() - 1; i >= 0; i--) {
            text.append(plainText.charAt(i));
        }
        plainText = text.toString();
        MessageDigest mDigest;
        try {
            mDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return plainText;
        }
        mDigest.update(plainText.getBytes());
        byte d[] = mDigest.digest();
        StringBuffer hash = new StringBuffer();
        for (int i = 0; i < d.length; i++) {
            hash.append(Integer.toHexString(0xFF & d[i]));
        }
        return hash.toString();
    }
