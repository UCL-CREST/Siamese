        public void encrypt(InputStream input, OutputStream output) throws EncryptionException {
            try {
                SecretKeySpec spec = new SecretKeySpec(encryptionKey, "AES");
                Cipher cipher = Cipher.getInstance("AES");
                byte[] buf = new byte[1024];
                int count;
                cipher.init(Cipher.ENCRYPT_MODE, spec);
                output = new CipherOutputStream(output, cipher);
                while ((count = input.read(buf)) >= 0) {
                    output.write(buf, 0, count);
                }
                output.close();
            } catch (GeneralSecurityException e) {
                throw new EncryptionException(e);
            } catch (IOException e) {
                throw new EncryptionException(e);
            }
        }
