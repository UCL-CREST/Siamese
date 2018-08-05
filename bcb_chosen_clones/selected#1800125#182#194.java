    private String MD5(String s) {
        Log.d("MD5", "Hashing '" + s + "'");
        String hash = "";
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            hash = new BigInteger(1, m.digest()).toString(16);
            Log.d("MD5", "Hash: " + hash);
        } catch (Exception e) {
            Log.e("MD5", e.getMessage());
        }
        return hash;
    }
