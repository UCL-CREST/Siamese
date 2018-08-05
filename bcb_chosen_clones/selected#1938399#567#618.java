    private boolean importPKC(String keystoreLocation, String pw, String pkcFile, String alias) {
        boolean imported = false;
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new BufferedInputStream(new FileInputStream(keystoreLocation)), pw.toCharArray());
        } catch (Exception e) {
            if (this.debugLevel >= SAWSConstant.ErrorInfo) {
                this.sawsDebugLog.write("Error reading keystore file when exporting PKC: " + e.getMessage());
            }
            return false;
        }
        Certificate cert = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(pkcFile));
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                cert = cf.generateCertificate(bis);
            }
        } catch (Exception e) {
            if (this.debugLevel >= SAWSConstant.ErrorInfo) {
                this.sawsDebugLog.write("Error reading certificate from file when importing PKC: " + e.getMessage());
            }
            return false;
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(new File(keystoreLocation)));
        } catch (FileNotFoundException e) {
            if (this.debugLevel >= SAWSConstant.ErrorInfo) {
                this.sawsDebugLog.write("Error accessing key store file when importing certificate: " + e.getMessage());
            }
            return false;
        }
        try {
            if (alias.equals("rootca")) {
                ks.setCertificateEntry(alias, cert);
            } else {
                KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(pw.toCharArray()));
                ks.setKeyEntry(alias, pkEntry.getPrivateKey(), pw.toCharArray(), new Certificate[] { cert });
            }
            ks.store(bos, pw.toCharArray());
            imported = true;
        } catch (Exception e) {
            e.printStackTrace();
            if (this.debugLevel >= SAWSConstant.ErrorInfo) {
                this.sawsDebugLog.write("Error writing keystore to file when importing key store: " + e.getMessage());
            }
            return false;
        }
        return imported;
    }
