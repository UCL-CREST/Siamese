    public OutputStream getEncryptedStream(OutputStream outputStream, char[] password) throws CipherException, IOException {
        OutputStream os = new BufferedOutputStream(outputStream);
        os.write(getHeader());
        if (isSaveDate()) os.write(ByteFunctions.longToByte(new Date().getTime()));
        byte[] salt = new byte[saltLength];
        if (password == null) {
            Arrays.fill(salt, (byte) '0');
        } else {
            try {
                SecureRandom secureRandom = SecureRandom.getInstance(secureRandomAlgorithm);
                secureRandom.nextBytes(salt);
            } catch (NoSuchAlgorithmException nsae) {
                throw new CipherException(nsae);
            }
        }
        os.write(salt);
        Cipher cipher;
        if (password == null) {
            cipher = new NullCipher();
        } else {
            try {
                byte[] passwordKey = getKeyFromPassword(password, salt, keyLength);
                SecretKeySpec key = new SecretKeySpec(passwordKey, keyAlgorithm);
                byte[] ivSpecKey = new byte[ivSpecLength];
                System.arraycopy(passwordKey, 0, ivSpecKey, 0, ivSpecKey.length);
                IvParameterSpec ivSpec = new IvParameterSpec(ivSpecKey);
                cipher = Cipher.getInstance(cipherAlgorithm);
                cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            } catch (Exception e) {
                throw new CipherException(e);
            }
        }
        OutputStream cos;
        if (isCompressData()) cos = new GZIPOutputStream(new CipherOutputStream(os, cipher)); else cos = new CipherOutputStream(os, cipher);
        cos.write(getCanary());
        return cos;
    }
