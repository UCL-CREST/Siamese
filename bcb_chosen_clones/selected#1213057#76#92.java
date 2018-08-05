    public static String md5(String source) {
        MessageDigest md;
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(source.getBytes());
            byte[] digested = md.digest();
            for (int i = 0; i < digested.length; i++) {
                pw.printf("%02x", digested[i]);
            }
            pw.flush();
            return sw.getBuffer().toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
