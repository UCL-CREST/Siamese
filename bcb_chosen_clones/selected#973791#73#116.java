    @Override
    public byte[] backupPassword(String uid, char[] password, String realm, String securityQuestion, char[] securityQuestionAnswer) {
        boolean passwordVerified = getLdapBean().verifyPassword(uid, password, realm);
        if (!passwordVerified) {
            throw new GatekeeperAuthenticationException("Backup password for: " + uid, uid, realm);
        }
        if (securityQuestion == null && securityQuestionAnswer != null) {
            throw new RuntimeException("Failed to backup password for: " + uid + " in realm: " + realm + ". A security question is provided without an answer");
        }
        PasswordBackup passwordBackup = findPasswordBackup(uid, realm);
        if (passwordBackup != null) {
            em.remove(passwordBackup);
        }
        if (securityQuestion == null) {
            return null;
        }
        try {
            String pbeKeyAlgorithm = PBE_KEY_ALGORITHM;
            byte[] salt = getRandomSalt();
            int iterationCount = Integer.parseInt(USER_PASSWORD_ITERATION_COUNT);
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
            } catch (Exception ex) {
                throw new RuntimeException("Could not not convert password from char[] to bytes", ex);
            }
            byte[] encryptedPassword = cipher.doFinal(baos.toByteArray());
            storeEncryptedPassword(encryptedPassword, salt, iterationCount, uid, realm, securityQuestion);
            return encryptedPassword;
        } catch (GeneralSecurityException ex) {
            throw new RuntimeException("Could not backup password for: " + uid, ex);
        }
    }
