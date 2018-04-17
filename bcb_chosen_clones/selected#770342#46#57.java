    public static String generate(String source) {
        byte[] SHA = new byte[20];
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(source.getBytes());
            SHA = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NO SUCH ALGORITHM EXCEPTION: " + e.getMessage());
        }
        CommunicationLogger.warning("SHA1 DIGEST: " + SHA);
        return SHA.toString();
    }
