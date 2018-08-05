    private String signMethod() {
        String str = API.SHARED_SECRET;
        Vector<String> v = new Vector<String>(parameters.keySet());
        Collections.sort(v);
        for (String key : v) {
            str += key + parameters.get(key);
        }
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
            m.update(str.getBytes(), 0, str.length());
            return new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
