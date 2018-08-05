    public static void encrypt(SecretKey key, Serializable obj, OutputStream out) throws IOException {
        byte[] dataBuf = serialize(obj);
        int msgLen = dataBuf.length;
        byte[] len = intToByteArray(msgLen);
        int totalLength = ((msgLen + 7) / 8) * 8;
        byte[] encryptBuf = new byte[totalLength];
        System.arraycopy(dataBuf, 0, encryptBuf, 0, msgLen);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES", "CryptixCrypto");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            addRandomPadding(encryptBuf, msgLen);
            encryptBuf = cipher.doFinal(encryptBuf);
            len = cipher.doFinal(len);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IOException("Cryptix library is not installed");
        }
        out.write(len);
        out.write(encryptBuf);
        out.flush();
    }
