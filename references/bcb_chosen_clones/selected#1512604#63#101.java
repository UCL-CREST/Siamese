    public static void appendBinFile(OutputStream out, File inputFile, byte[] key, boolean encrypt) throws FileNotFoundException, IOException, InvalidKeyException {
        try {
            BufferedOutputStream bout = new BufferedOutputStream(out);
            FileInputStream in = new FileInputStream(inputFile);
            byte[] buffer = new byte[4096];
            int size;
            final String CipherAlgorithmName = "AES";
            final String CipherAlgorithmMode = "CBC";
            final String CipherAlgorithmPadding = "PKCS5Padding";
            final String CryptoTransformation = CipherAlgorithmName + "/" + CipherAlgorithmMode + "/" + CipherAlgorithmPadding;
            SecretKeySpec ky = new SecretKeySpec(key, CipherAlgorithmName);
            byte[] iv = new byte[16];
            AlgorithmParameterSpec aps = new IvParameterSpec(iv);
            Cipher cf = Cipher.getInstance(CryptoTransformation);
            if (encrypt) cf.init(Cipher.ENCRYPT_MODE, ky, aps); else cf.init(Cipher.DECRYPT_MODE, ky, aps);
            while ((size = in.read(buffer)) != -1) {
                byte[] bufPost;
                if (size != buffer.length) {
                    try {
                        bufPost = cf.doFinal(buffer, 0, size);
                    } catch (IllegalBlockSizeException ex) {
                        throw new RuntimeException(ex);
                    } catch (BadPaddingException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    bufPost = cf.update(buffer, 0, size);
                }
                bout.write(bufPost);
            }
            bout.close();
        } catch (InvalidAlgorithmParameterException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchPaddingException ex) {
            throw new RuntimeException(ex);
        }
    }
