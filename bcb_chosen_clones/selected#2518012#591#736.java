    private void modifyDialog(boolean fileExists) {
        if (fileExists) {
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_REVOCATION_LOCATION)) {
                RevLocation = ((String) vars.get(EnvironmentalVariables.WEBDAV_REVOCATION_LOCATION));
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_CERTIFICATE_LOCATION)) {
                CertLocation = ((String) vars.get(EnvironmentalVariables.WEBDAV_CERTIFICATE_LOCATION));
            }
            if (vars.containsKey(EnvironmentalVariables.HOLDER_NAME_STRING)) {
                jHolderName.setText((String) vars.get(EnvironmentalVariables.HOLDER_NAME_STRING));
            } else jHolderName.setText("<EMPTY>");
            if (vars.containsKey(EnvironmentalVariables.LDAP_HOLDER_EDITOR_UTILITY)) {
                if (vars.containsKey(EnvironmentalVariables.HOLDER_EDITOR_UTILITY_SERVER)) {
                    jProviderURL.setText((String) vars.get(EnvironmentalVariables.HOLDER_EDITOR_UTILITY_SERVER));
                }
            }
            if (vars.containsKey(EnvironmentalVariables.SERIAL_NUMBER_STRING)) {
                serialNumber = (String) vars.get(EnvironmentalVariables.SERIAL_NUMBER_STRING);
            } else serialNumber = "<EMPTY>";
            if (vars.containsKey(EnvironmentalVariables.VALIDITY_PERIOD_STRING)) {
                jValidityPeriod.setText((String) vars.get(EnvironmentalVariables.VALIDITY_PERIOD_STRING));
            } else jValidityPeriod.setText("<EMPTY>");
            if (vars.containsKey(LDAPSavingUtility.LDAP_SAVING_UTILITY_AC_TYPE)) {
                String acType = (String) vars.get(LDAPSavingUtility.LDAP_SAVING_UTILITY_AC_TYPE);
                if ((!acType.equals("")) && (!acType.equals("<EMPTY>"))) jACType.setText((String) vars.get(LDAPSavingUtility.LDAP_SAVING_UTILITY_AC_TYPE)); else jACType.setText("attributeCertificateAttribute");
            }
            if (utils.containsKey("issrg.acm.extensions.SimpleSigningUtility")) {
                if (vars.containsKey(DefaultSecurity.DEFAULT_FILE_STRING)) {
                    jDefaultProfile.setText((String) vars.get(DefaultSecurity.DEFAULT_FILE_STRING));
                } else jDefaultProfile.setText("<EMPTY>");
                jCHEntrust.setSelected(true);
            } else {
                jCHEntrust.setSelected(false);
                jDefaultProfile.setEnabled(false);
            }
            if (utils.containsKey("issrg.acm.extensions.ACMDISSigningUtility")) {
                if (vars.containsKey("DefaultDIS")) {
                    jDISAddress.setText((String) vars.get("DefaultDIS"));
                } else jDISAddress.setText("<EMPTY>");
                jDIS.setSelected(true);
                jCHEntrust.setSelected(true);
                jDefaultProfile.setEnabled(true);
                if (vars.containsKey(DefaultSecurity.DEFAULT_FILE_STRING)) {
                    jDefaultProfile.setText((String) vars.get(DefaultSecurity.DEFAULT_FILE_STRING));
                } else jDefaultProfile.setText("permis.p12");
            } else {
                jDIS.setSelected(false);
                jDISAddress.setEnabled(false);
            }
            if (vars.containsKey(EnvironmentalVariables.AAIA_LOCATION)) {
                jaaia[0].setSelected(true);
            }
            if (vars.containsKey(EnvironmentalVariables.NOREV_LOCATION)) {
                jnorev[0].setSelected(true);
                jdavrev[0].setEnabled(false);
                jdavrev[1].setEnabled(false);
                jdavrev[1].setSelected(false);
            }
            if (vars.containsKey(EnvironmentalVariables.DAVREV_LOCATION)) {
                jdavrev[0].setSelected(true);
                jnorev[0].setEnabled(false);
                jnorev[1].setEnabled(false);
                jnorev[1].setSelected(true);
            }
            if (vars.containsKey("LDAPSavingUtility.ProviderURI")) {
                jProviderURL.setText((String) vars.get("LDAPSavingUtility.ProviderURI"));
            } else jProviderURL.setText("<EMPTY>");
            if (vars.containsKey("LDAPSavingUtility.Login")) {
                jProviderLogin.setText((String) vars.get("LDAPSavingUtility.Login"));
            } else jProviderLogin.setText("<EMPTY>");
            if (vars.containsKey("LDAPSavingUtility.Password")) {
                jProviderPassword.setText((String) vars.get("LDAPSavingUtility.Password"));
            } else jProviderPassword.setText("<EMPTY>");
            if ((!vars.containsKey(EnvironmentalVariables.TRUSTSTORE)) || (((String) vars.get(EnvironmentalVariables.TRUSTSTORE)).equals(""))) {
                vars.put(EnvironmentalVariables.TRUSTSTORE, "truststorefile");
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_HOST)) {
                jWebDAVHost.setText((String) vars.get(EnvironmentalVariables.WEBDAV_HOST));
            } else {
                jWebDAVHost.setText("<EMPTY>");
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_PORT)) {
                jWebDAVPort.setText((String) vars.get(EnvironmentalVariables.WEBDAV_PORT));
            } else {
                jWebDAVPort.setText("<EMPTY>");
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_PROTOCOL)) {
                if (vars.get(EnvironmentalVariables.WEBDAV_PROTOCOL).equals("HTTPS")) {
                    jWebDAVHttps.setSelected(true);
                    jWebDAVSelectP12.setEnabled(true);
                    jWebDAVP12Filename.setEnabled(true);
                    jWebDAVP12Password.setEnabled(true);
                    jWebDAVSSL.setEnabled(true);
                    addWebDAVSSL.setEnabled(true);
                } else {
                    jWebDAVHttps.setSelected(false);
                    jWebDAVSelectP12.setEnabled(false);
                    jWebDAVP12Filename.setEnabled(false);
                    jWebDAVP12Password.setEnabled(false);
                    jWebDAVSSL.setEnabled(false);
                    addWebDAVSSL.setEnabled(false);
                }
            } else {
                jWebDAVHttps.setSelected(false);
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_P12FILENAME)) {
                jWebDAVP12Filename.setText((String) vars.get(EnvironmentalVariables.WEBDAV_P12FILENAME));
            } else {
                jWebDAVP12Filename.setText("<EMPTY>");
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_P12PASSWORD)) {
                jWebDAVP12Password.setText((String) vars.get(EnvironmentalVariables.WEBDAV_P12PASSWORD));
            } else {
                jWebDAVP12Password.setText("<EMPTY>");
            }
            if (vars.containsKey(EnvironmentalVariables.WEBDAV_SSLCERTIFICATE)) {
                jWebDAVSSL.setText((String) vars.get(EnvironmentalVariables.WEBDAV_SSLCERTIFICATE));
            } else {
                jWebDAVSSL.setText("<EMPTY>");
            }
        } else {
            jHolderName.setText("cn=A Permis Test User, o=PERMIS, c=gb");
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(new Date().toString().getBytes());
                byte[] result = md.digest();
                BigInteger bi = new BigInteger(result);
                bi = bi.abs();
                serialNumber = bi.toString(16);
            } catch (Exception e) {
                serialNumber = "<EMPTY>";
            }
            jValidityPeriod.setText("<EMPTY>");
            jDefaultProfile.setText("permis.p12");
            jCHEntrust.setSelected(true);
            jProviderURL.setText("ldap://sec.cs.kent.ac.uk/c=gb");
            jProviderLogin.setText("");
            jProviderPassword.setText("");
            jWebDAVHost.setText("");
            jWebDAVPort.setText("443");
            jWebDAVP12Filename.setText("");
            jACType.setText("attributeCertificateAttribute");
            vars.put(EnvironmentalVariables.TRUSTSTORE, "truststorefile");
            saveChanges();
        }
    }
