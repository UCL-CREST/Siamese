    public static String generate(String source) {
        byte[] SHA = new byte[20];
        String SHADigest = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(source.getBytes());
            SHA = digest.digest();
            for (int i = 0; i < SHA.length; i++) SHADigest += (char) SHA[i];
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NO SUCH ALGORITHM EXCEPTION: " + e.getMessage());
        }
        return SHADigest;
    }
