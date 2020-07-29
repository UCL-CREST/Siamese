    private boolean exportPKC(String keystoreLocation, String pw) {
        boolean created = false;
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
            cert = ks.getCertificate("saws");
        } catch (KeyStoreException e) {
            if (this.debugLevel >= SAWSConstant.ErrorInfo) {
                this.sawsDebugLog.write("Error reading certificate from keystore file when exporting PKC: " + e.getMessage());
            }
            return false;
        }
        try {
            StringBuffer sb = new StringBuffer("-----BEGIN CERTIFICATE-----\n");
            sb.append(new String(Base64.encode(cert.getEncoded())));
            sb.append("\n-----END CERTIFICATE-----\n");
            OutputStreamWriter wr = new OutputStreamWriter(new FileOutputStream("sawsSigningPKC.crt"));
            wr.write(new String(sb));
            wr.flush();
            wr.close();
            created = true;
        } catch (Exception e) {
            if (this.debugLevel >= SAWSConstant.ErrorInfo) {
                this.sawsDebugLog.write("Error exporting PKC file: " + e.getMessage());
            }
            return false;
        }
        return created;
    }
