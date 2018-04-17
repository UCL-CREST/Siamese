    public static String encrypt(String passPhrase, String password) {
        String algorithm = "PBEWithMD5AndDES";
        byte[] salt = new byte[8];
        int iterations = 20;
        byte[] output = new byte[128];
        if (passPhrase == null || "".equals(passPhrase) || password == null || "".equals(password)) {
            throw new GeneralException(PassPhraseCrypto.class, "encrypt", "Required parameter missing");
        }
        try {
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray());
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(passPhrase.getBytes());
            byte[] input = new byte[password.length()];
            input = password.getBytes();
            messageDigest.update(input);
            byte[] digest = messageDigest.digest();
            System.arraycopy(digest, 0, salt, 0, 8);
            AlgorithmParameterSpec algorithmParameterSpec = new PBEParameterSpec(salt, iterations);
            Cipher cipher = Cipher.getInstance(algorithm);
            int mode = Cipher.ENCRYPT_MODE;
            cipher.init(mode, secretKey, algorithmParameterSpec);
            output = cipher.doFinal(input);
        } catch (NoSuchAlgorithmException e) {
            throw new GeneralException(PassPhraseCrypto.class, "encrypt", "Algorithm not found", e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new GeneralException(PassPhraseCrypto.class, "encrypt", "nvalidAlgorithmParameter", e);
        } catch (InvalidKeySpecException e) {
            throw new GeneralException(PassPhraseCrypto.class, "encrypt", "InvalidKeySpec", e);
        } catch (InvalidKeyException e) {
            throw new GeneralException(PassPhraseCrypto.class, "encrypt", "InvalidKey", e);
        } catch (NoSuchPaddingException e) {
            throw new GeneralException(PassPhraseCrypto.class, "encrypt", "NoSuchPadding", e);
        } catch (BadPaddingException e) {
            throw new GeneralException(PassPhraseCrypto.class, "encrypt", "BadPadding", e);
        } catch (IllegalBlockSizeException e) {
            throw new GeneralException(PassPhraseCrypto.class, "encrypt", "IllegalBlockSize", e);
        }
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < output.length; i++) {
            result.append(Byte.toString(output[i]));
        }
        return result.toString();
    }
