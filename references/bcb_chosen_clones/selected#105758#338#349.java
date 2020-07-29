    @edu.umd.cs.findbugs.annotations.SuppressWarnings({ "DLS", "REC" })
    public static String shaEncode(String val) {
        String output = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(val.getBytes());
            byte[] digest = md.digest();
            output = base64Encode(digest);
        } catch (Exception e) {
        }
        return output;
    }
