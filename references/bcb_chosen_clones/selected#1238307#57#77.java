    public Serializable onMessage(Serializable msg) throws IOException {
        try {
            BidirectionalChannel channel = connect();
            SecretKey sessionKey = gen.generateKey();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS#1", "CryptixCrypto");
            cipher.init(Cipher.ENCRYPT_MODE, serverKey);
            byte[] encoded = sessionKey.getEncoded();
            byte[] header = cipher.doFinal(encoded);
            channel.getOutputStream().write(header);
            Util.encrypt(sessionKey, new TimeStampedEnvelope(msg), channel.getOutputStream());
            Serializable ret = Util.decrypt(sessionKey, channel.getInputStream());
            channel.close();
            return ret;
        } catch (KeyException e) {
            e.printStackTrace();
            throw new IOException("Cryptix library is not installed");
        } catch (GeneralSecurityException e) {
            e.printStackTrace(System.err);
            throw new InternalError();
        }
    }
