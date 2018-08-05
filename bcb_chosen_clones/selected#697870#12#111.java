    public static DuplexChannel secureChannel(DuplexChannel channel, EncryptionModel localModel) throws IOException {
        if (!localModel.isComplete()) throw new IllegalArgumentException("Modello di sicurezza incompleto");
        InputStream plain_is = channel.getInputStream();
        OutputStream plain_os = channel.getOutputStream();
        InputStream crypt_is = null;
        OutputStream crypt_os = null;
        DuplexChannel result = null;
        byte[] cache = new byte[4];
        try {
            localModel.marshal(plain_os);
            EncryptionModel remoteModel = EncryptionModel.unmarshal(plain_is);
            KeyPair kpair = null;
            if (channel.getIdentity() != null) {
                kpair = localModel.regenKeyPair(channel.getIdentity().getPrivateLocalKey(), channel.getIdentity().getPublicLocalKey());
            } else {
                kpair = localModel.createKeyPair();
            }
            PrivateKey localPrivateKey = kpair.getPrivate();
            localModel.marshalPublicKey(kpair.getPublic(), plain_os);
            Cipher asyReceivingCipher = Cipher.getInstance(localModel.getAsyCipherAlgorithm());
            asyReceivingCipher.init(Cipher.DECRYPT_MODE, localPrivateKey);
            PublicKey remotePublicKey = remoteModel.unmarshalPublicKey(plain_is);
            if (channel.getIdentity() != null) {
                byte[] expectedRemoteKey = channel.getIdentity().getPublicRemoteKey();
                if (expectedRemoteKey != null) {
                    if (!(Arrays.equals(expectedRemoteKey, remoteModel.getPublicKeySpec(remotePublicKey)))) throw new IllegalStateException("Chave pubblica ricevuta diversa da quella attesa");
                }
            }
            Cipher asySendingCipher = Cipher.getInstance(remoteModel.getAsyCipherAlgorithm());
            asySendingCipher.init(Cipher.ENCRYPT_MODE, remotePublicKey);
            Key localSecKey = localModel.createSecretKey();
            byte[] localSecKeyBytes = localSecKey.getEncoded();
            Cipher symSendingCipher = Cipher.getInstance(localModel.getSymCipherAlgorithm());
            symSendingCipher.init(Cipher.ENCRYPT_MODE, localSecKey);
            byte[] localCipherParams = symSendingCipher.getParameters().getEncoded();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(asySendingCipher.update(Bitwise.intToByteArray(localSecKeyBytes.length)));
            bos.write(asySendingCipher.update(localSecKeyBytes));
            bos.write(asySendingCipher.update(Bitwise.intToByteArray(localCipherParams.length)));
            bos.write(asySendingCipher.doFinal(localCipherParams));
            bos.flush();
            bos.close();
            byte[] symLocalInitBlock = bos.toByteArray();
            plain_os.write(Bitwise.intToByteArray(symLocalInitBlock.length));
            plain_os.write(symLocalInitBlock);
            plain_os.flush();
            plain_is.read(cache);
            byte[] symRemoteInitBlock = new byte[Bitwise.byteArrayToInt(cache)];
            plain_is.read(symRemoteInitBlock);
            ByteArrayInputStream bis = new ByteArrayInputStream(asyReceivingCipher.doFinal(symRemoteInitBlock));
            bis.read(cache);
            byte[] remoteSecKeyBytes = new byte[Bitwise.byteArrayToInt(cache)];
            bis.read(remoteSecKeyBytes);
            bis.read(cache);
            byte[] remoteCipherParams = new byte[Bitwise.byteArrayToInt(cache)];
            bis.read(remoteCipherParams);
            bis.close();
            Key remoteSecKey = remoteModel.regenSecretKey(remoteSecKeyBytes);
            AlgorithmParameters aParams = AlgorithmParameters.getInstance(remoteModel.getSecretKeyAlgorithm());
            aParams.init(remoteCipherParams);
            Cipher symReceivingCipher = Cipher.getInstance(remoteModel.getSymCipherAlgorithm());
            symReceivingCipher.init(Cipher.DECRYPT_MODE, remoteSecKey, aParams);
            crypt_is = new CipherInputStream(plain_is, symReceivingCipher);
            crypt_os = new CipherOutputStream(plain_os, symSendingCipher);
            Identity finalId = new Identity();
            finalId.setPublicRemoteKey(remoteModel.getPublicKeySpec(remotePublicKey));
            finalId.setPublicLocalKey(localModel.getPublicKeySpec(kpair.getPublic()));
            finalId.setPrivateLocalKey(localModel.getPrivateKeySpec(kpair.getPrivate()));
            result = new DuplexChannel(crypt_is, crypt_os, finalId);
        } catch (NoSuchAlgorithmException ex) {
            IOException ioex = new IOException();
            ioex.initCause(ex);
            throw ioex;
        } catch (InvalidKeySpecException ex) {
            IOException ioex = new IOException();
            ioex.initCause(ex);
            throw ioex;
        } catch (NoSuchPaddingException ex) {
            IOException ioex = new IOException();
            ioex.initCause(ex);
            throw ioex;
        } catch (InvalidKeyException ex) {
            IOException ioex = new IOException();
            ioex.initCause(ex);
            throw ioex;
        } catch (IllegalBlockSizeException ex) {
            IOException ioex = new IOException();
            ioex.initCause(ex);
            throw ioex;
        } catch (BadPaddingException ex) {
            IOException ioex = new IOException();
            ioex.initCause(ex);
            throw ioex;
        } catch (InvalidAlgorithmParameterException ex) {
            IOException ioex = new IOException();
            ioex.initCause(ex);
            throw ioex;
        }
        return result;
    }
