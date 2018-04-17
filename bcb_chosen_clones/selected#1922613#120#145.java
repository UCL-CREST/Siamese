    public static byte[] symmetricEncrypt(SecretKey encryptionKey, FileInputStream fis) throws IOException {
        try {
            Cipher cipher = Cipher.getInstance(SYMMETRIC_ALG);
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(fis.available());
            int bytes = fis.available();
            while (bytes > 0) {
                byte[] buffer = new byte[bytes];
                fis.read(buffer);
                baos.write(cipher.update(buffer));
                bytes = fis.available();
            }
            baos.write(cipher.doFinal());
            return baos.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        } catch (NoSuchPaddingException e) {
            throw new Error(e);
        } catch (InvalidKeyException e) {
            throw new Error(e);
        } catch (IllegalBlockSizeException e) {
            throw new Error(e);
        } catch (BadPaddingException e) {
            throw new Error(e);
        }
    }
