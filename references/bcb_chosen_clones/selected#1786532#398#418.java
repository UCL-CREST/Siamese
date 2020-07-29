    public static String generateSig(Map<String, String> params, String secret) {
        SortedSet<String> keys = new TreeSet<String>(params.keySet());
        keys.remove(FacebookParam.SIGNATURE.toString());
        String str = "";
        for (String key : keys) {
            str += key + "=" + params.get(key);
        }
        str += secret;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes("UTF-8"));
            StringBuilder result = new StringBuilder();
            for (byte b : md.digest()) {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
            return result.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
