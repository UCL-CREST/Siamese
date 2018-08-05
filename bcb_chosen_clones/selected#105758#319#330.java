    @edu.umd.cs.findbugs.annotations.SuppressWarnings({ "DLS", "REC" })
    public static String md5Encode(String val) {
        String output = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(val.getBytes());
            byte[] digest = md.digest();
            output = base64Encode(digest);
        } catch (Exception e) {
        }
        return output;
    }
