    protected static final byte[] digest(String s) {
        byte[] ret = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());
            ret = md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("no message digest algorithm available!");
            System.exit(1);
        }
        return ret;
    }
