    static boolean generateKey() throws NoSuchAlgorithmException {
        java.util.Random rand = new Random(reg_name.hashCode() + System.currentTimeMillis());
        DecimalFormat vf = new DecimalFormat("000");
        ccKey = keyProduct + FIELD_SEPERATOR + keyType + FIELD_SEPERATOR + keyQuantity + FIELD_SEPERATOR + vf.format(lowMajorVersion) + FIELD_SEPERATOR + vf.format(lowMinorVersion) + FIELD_SEPERATOR + vf.format(highMajorVersion) + FIELD_SEPERATOR + vf.format(highMinorVersion) + FIELD_SEPERATOR + reg_name + FIELD_SEPERATOR + Integer.toHexString(rand.nextInt()).toUpperCase();
        byte[] md5;
        MessageDigest md = null;
        md = MessageDigest.getInstance("MD5");
        md.update(ccKey.getBytes());
        md.update(FIELD_SEPERATOR.getBytes());
        md.update(zuonicsPassword.getBytes());
        md5 = md.digest();
        userKey = ccKey + FIELD_SEPERATOR;
        for (int i = 0; i < md5.length; i++) userKey += Integer.toHexString(md5[i]).toUpperCase();
        return true;
    }
