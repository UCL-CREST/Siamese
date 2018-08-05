    public static String generateCode(String seed) {
        try {
            Security.addProvider(new FNVProvider());
            MessageDigest digest = MessageDigest.getInstance("FNV-1a");
            digest.update((seed + UUID.randomUUID().toString()).getBytes());
            byte[] hash1 = digest.digest();
            String sHash1 = "m" + (new String(LibraryBase64.encode(hash1))).replaceAll("=", "");
            return sHash1;
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Internal error:" + e.getMessage());
            return null;
        }
    }
