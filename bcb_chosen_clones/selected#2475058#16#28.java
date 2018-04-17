    private static String getHash(String hash, String clear) {
        try {
            MessageDigest md = MessageDigest.getInstance(hash);
            md.update(clear.getBytes("UTF-8"));
            byte[] bytes = md.digest();
            String str = new String();
            for (int i = 0; i < bytes.length; ++i) str += Integer.toHexString(0xF0 & bytes[i]).charAt(0) + Integer.toHexString(0x0F & bytes[i]);
            return str;
        } catch (NoSuchAlgorithmException exc) {
        } catch (UnsupportedEncodingException exc) {
        }
        return "";
    }
