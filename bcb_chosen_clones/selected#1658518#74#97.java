    public static byte[] decode(String cryptPassword, byte[] encoded, byte[] salt) {
        try {
            MessageDigest digester = MessageDigest.getInstance(DIGEST);
            SecureRandom random = SecureRandom.getInstance(RANDOM);
            digester.reset();
            for (int i = 0; i < ITERATIONS; i++) {
                digester.update(salt);
                digester.update(cryptPassword.getBytes("UTF-8"));
            }
            byte[] hash = digester.digest();
            random.setSeed(hash);
            int maxKeySize = Cipher.getMaxAllowedKeyLength(CIPHER);
            KeyGenerator generator = KeyGenerator.getInstance(CIPHER);
            generator.init(maxKeySize, random);
            SecretKey key = generator.generateKey();
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = cipher.doFinal(encoded);
            return decoded;
        } catch (Exception e) {
            StorePlugin.getDefault().log(e);
        }
        return new byte[0];
    }
