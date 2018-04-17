    private String buildShaHashOf(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(source.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
