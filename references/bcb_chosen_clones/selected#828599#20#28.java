    public static String sha1(String clearText, String seed) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update((seed + clearText).getBytes());
            return convertToHex(md.digest());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
