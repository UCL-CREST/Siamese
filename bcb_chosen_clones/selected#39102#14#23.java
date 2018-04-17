    public String Hash(String plain) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(plain.getBytes(), 0, plain.length());
            return new BigInteger(1, md5.digest()).toString(16);
        } catch (Exception ex) {
            Log.serverlogger.warn("No such Hash algorithm", ex);
            return "";
        }
    }
