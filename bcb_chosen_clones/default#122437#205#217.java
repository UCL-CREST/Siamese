    private boolean keysMatch(String keyNMinusOne, String keyN) {
        boolean match = false;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(keyNMinusOne.getBytes());
            byte[] hashedBytes = digest.digest();
            String encodedHashedKey = new String(com.Ostermiller.util.Base64.encode(hashedBytes));
            match = encodedHashedKey.equals(keyN);
        } catch (NoSuchAlgorithmException e) {
        }
        return match;
    }
