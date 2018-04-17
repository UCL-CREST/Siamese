    public static String base64HashedString(String v) {
        String base64HashedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(v.getBytes());
            String hashedPassword = new String(md.digest());
            sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
            base64HashedPassword = enc.encode(hashedPassword.getBytes());
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new NSForwardException(e, "Couldn't find the SHA hash algorithm; perhaps you do not have the SunJCE security provider installed properly?");
        }
        return base64HashedPassword;
    }
