    private String generateFilename() {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            try {
                digest.update(InetAddress.getLocalHost().toString().getBytes());
            } catch (UnknownHostException e) {
            }
            digest.update(String.valueOf(System.currentTimeMillis()).getBytes());
            digest.update(String.valueOf(Runtime.getRuntime().freeMemory()).getBytes());
            byte[] foo = new byte[128];
            new SecureRandom().nextBytes(foo);
            digest.update(foo);
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            Debug.assrt(false);
        }
        return hexEncode(hash);
    }
