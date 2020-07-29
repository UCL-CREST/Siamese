    public static String generateStackHashKey() {
        Exception e = null;
        try {
            throw new Exception();
        } catch (Exception ex) {
            e = ex;
        }
        MessageDigest digest;
        try {
            digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(getStackTrace(e).getBytes());
            byte[] hash = digest.digest();
            String rtn = Base64.encode(new String(hash));
            if (keys.contains(rtn)) {
                return generatedIterStackHashKey(rtn);
            }
            keys.add(rtn);
            return rtn;
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
