    public static String digestString(String data) {
        String result = null;
        if (data != null) {
            try {
                MessageDigest _md = MessageDigest.getInstance("MD5");
                _md.update(data.getBytes());
                byte[] _digest = _md.digest();
                String _ds = toHexString(_digest, 0, _digest.length);
                result = _ds;
            } catch (NoSuchAlgorithmException e) {
                result = null;
            }
        }
        return result;
    }
