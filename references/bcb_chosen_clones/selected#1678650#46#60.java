    public static String getCheckValueForCellLogin(String username, String userid) {
        if (username == null || username.length() == 0 || userid == null) {
            return "";
        }
        String marge = username + userid;
        CRC32 crc32 = new CRC32();
        crc32.update(marge.getBytes());
        long value = crc32.getValue();
        String base64value = null;
        try {
            base64value = new String(Base64.encodeBase64(String.valueOf(value).getBytes()));
        } catch (Exception e) {
        }
        return (base64value == null) ? "" : base64value.toLowerCase();
    }
