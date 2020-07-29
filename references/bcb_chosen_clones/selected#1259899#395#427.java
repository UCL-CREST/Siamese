    public final synchronized boolean isValidLicenseFile() throws LicenseNotSetupException {
        if (!isSetup()) {
            throw new LicenseNotSetupException();
        }
        boolean returnValue = false;
        Properties properties = getLicenseFile();
        logger.debug("isValidLicenseFile: License to validate:");
        logger.debug(properties);
        StringBuffer validationStringBuffer = new StringBuffer();
        validationStringBuffer.append(LICENSE_KEY_KEY + ":" + properties.getProperty(LICENSE_KEY_KEY) + ",");
        validationStringBuffer.append(LICENSE_FILE_STATUS_KEY + ":" + properties.getProperty(LICENSE_FILE_STATUS_KEY) + ",");
        validationStringBuffer.append(LICENSE_FILE_USERS_KEY + ":" + properties.getProperty(LICENSE_FILE_USERS_KEY) + ",");
        validationStringBuffer.append(LICENSE_FILE_MAC_KEY + ":" + properties.getProperty(LICENSE_FILE_MAC_KEY) + ",");
        validationStringBuffer.append(LICENSE_FILE_HOST_NAME_KEY + ":" + properties.getProperty(LICENSE_FILE_HOST_NAME_KEY) + ",");
        validationStringBuffer.append(LICENSE_FILE_OFFSET_KEY + ":" + properties.getProperty(LICENSE_FILE_OFFSET_KEY) + ",");
        validationStringBuffer.append(LICENSE_FILE_EXP_DATE_KEY + ":" + properties.getProperty(LICENSE_FILE_EXP_DATE_KEY) + ",");
        validationStringBuffer.append(LICENSE_EXPIRES_KEY + ":" + properties.getProperty(LICENSE_EXPIRES_KEY));
        logger.debug("isValidLicenseFile: Validation String Buffer: " + validationStringBuffer.toString());
        String validationKey = (String) properties.getProperty(LICENSE_FILE_SHA_KEY);
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(validationStringBuffer.toString().getBytes());
            String newValidation = Base64.encode(messageDigest.digest());
            if (newValidation.equals(validationKey)) {
                if (getMACAddress().equals(Settings.getInstance().getMACAddress())) {
                    returnValue = true;
                }
            }
        } catch (Exception exception) {
            System.out.println("Exception in LicenseInstanceVO.isValidLicenseFile");
        }
        return returnValue;
    }
