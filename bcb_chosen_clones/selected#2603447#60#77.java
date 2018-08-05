    public String getSHA1Hex(String original) {
        MessageDigest digest;
        String result = "";
        try {
            digest = MessageDigest.getInstance("sha1");
            digest.reset();
            digest.update(original.getBytes());
            byte[] dig = digest.digest();
            String hexStr = "";
            for (int i = 0; i < dig.length; i++) {
                hexStr += Integer.toString((dig[i] & 0xff) + 0x100, 16).substring(1);
            }
            result = hexStr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
