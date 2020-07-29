    private String generateCode(String seed) {
        try {
            Security.addProvider(new FNVProvider());
            MessageDigest digest = MessageDigest.getInstance("FNV-1a");
            digest.update((seed + UUID.randomUUID().toString()).getBytes());
            byte[] hash1 = digest.digest();
            String sHash1 = "m" + (new String(LibraryBase64.encode(hash1))).replaceAll("=", "").replaceAll("-", "_");
            return sHash1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
