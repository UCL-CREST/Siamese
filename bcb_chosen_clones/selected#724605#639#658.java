        public void engineStore(OutputStream stream, char[] password) throws IOException {
            Cipher cipher;
            DataOutputStream dOut = new DataOutputStream(stream);
            byte[] salt = new byte[STORE_SALT_SIZE];
            int iterationCount = MIN_ITERATIONS + (random.nextInt() & 0x3ff);
            random.nextBytes(salt);
            dOut.writeInt(STORE_VERSION);
            dOut.writeInt(salt.length);
            dOut.write(salt);
            dOut.writeInt(iterationCount);
            cipher = this.makePBECipher(STORE_CIPHER, Cipher.ENCRYPT_MODE, password, salt, iterationCount);
            CipherOutputStream cOut = new CipherOutputStream(dOut, cipher);
            DigestOutputStream dgOut = new DigestOutputStream(cOut, new SHA1Digest());
            this.saveStore(dgOut);
            Digest dig = dgOut.getDigest();
            byte[] hash = new byte[dig.getDigestSize()];
            dig.doFinal(hash, 0);
            cOut.write(hash);
            cOut.close();
        }
