    public static byte[] encode(String cryptPassword, byte[] credentials, byte[] salt) {
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
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherOut = cipher.doFinal(credentials);
            byte[] ret = new byte[salt.length + cipherOut.length];
            System.arraycopy(salt, 0, ret, 0, salt.length);
            System.arraycopy(cipherOut, 0, ret, salt.length, cipherOut.length);
            return ret;
        } catch (Exception e) {
            StorePlugin.getDefault().log(e);
        }
        return new byte[0];
    }
