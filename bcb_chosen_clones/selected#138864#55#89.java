    public static String encrypt(String unencryptedString) {
        if (StringUtils.isBlank(unencryptedString)) {
            throw new IllegalArgumentException("Cannot encrypt a null or empty string");
        }
        MessageDigest md = null;
        String encryptionAlgorithm = Environment.getValue(Environment.PROP_ENCRYPTION_ALGORITHM);
        try {
            md = MessageDigest.getInstance(encryptionAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            logger.warn("JDK does not support the " + encryptionAlgorithm + " encryption algorithm.  Weaker encryption will be attempted.");
        }
        if (md == null) {
            try {
                md = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new UnsupportedOperationException("JDK does not support the SHA-1 or SHA-512 encryption algorithms");
            }
            Environment.setValue(Environment.PROP_ENCRYPTION_ALGORITHM, "SHA-1");
            try {
                Environment.saveConfiguration();
            } catch (WikiException e) {
                logger.info("Failure while saving encryption algorithm property", e);
            }
        }
        try {
            md.update(unencryptedString.getBytes("UTF-8"));
            byte raw[] = md.digest();
            return encrypt64(raw);
        } catch (GeneralSecurityException e) {
            logger.error("Encryption failure", e);
            throw new IllegalStateException("Failure while encrypting value");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unsupporting encoding UTF-8");
        }
    }
