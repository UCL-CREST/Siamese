    public static String encrypt(String pass, byte[] plainText) throws Exception {
        int iterationCount = 19;
        KeySpec keySpec = new PBEKeySpec(pass.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        byte[] enc = ecipher.doFinal(plainText);
        ByteArrayOutputStream byteArrayOutpuStream = new ByteArrayOutputStream();
        Base64OutputStream encoder = new Base64OutputStream(byteArrayOutpuStream, true);
        encoder.write(enc);
        encoder.flush();
        encoder.close();
        return byteArrayOutpuStream.toString();
    }
