    private byte[] backupPassword(String userLdapUID, char[] password, String passwordPurpose, String securityQuestion, char[] securityQuestionAnswer) {
        try {
            String pbeKeyAlgorithm = propertiesBean.getProperty(PBE_KEY_ALGORITHM_PROP);
            byte[] salt = getRandomSalt();
            int iterationCount = Integer.parseInt(propertiesBean.getProperty(USER_PASSWORD_ITERATION_COUNT_PROP));
            PBEKeySpec pbeKeySpec = new PBEKeySpec(securityQuestionAnswer);
            SecretKey secretKey = SecretKeyFactory.getInstance(pbeKeyAlgorithm).generateSecret(pbeKeySpec);
            PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, iterationCount);
            Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParamSpec);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter outputStreamWriter = null;
            try {
                try {
                    outputStreamWriter = new OutputStreamWriter(baos, Charset.forName("UTF-8"));
                    outputStreamWriter.write(password, 0, password.length);
                } finally {
                    outputStreamWriter.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException("Could not not convert password from char[] to bytes", ex);
            }
            byte[] encryptedPassword = cipher.doFinal(baos.toByteArray());
            storeEncryptedPassword(encryptedPassword, salt, iterationCount, userLdapUID, securityQuestion, passwordPurpose);
            return encryptedPassword;
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException("Could not backup password for: " + userLdapUID, ex);
        }
    }
