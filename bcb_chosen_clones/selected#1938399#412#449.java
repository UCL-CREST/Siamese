    private boolean createPKCReqest(String keystoreLocation, String pw) {
        boolean created = false;
        Security.addProvider(new BouncyCastleProvider());
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(new BufferedInputStream(new FileInputStream(keystoreLocation)), pw.toCharArray());
        } catch (Exception e) {
            if (this.debugLevel >= SAWSConstant.ErrorInfo) {
                this.sawsDebugLog.write("Error reading keystore file when creating PKC request: " + e.getMessage());
            }
            return false;
        }
        Certificate cert = null;
        try {
            cert = ks.getCertificate("saws");
        } catch (KeyStoreException e) {
            if (this.debugLevel >= SAWSConstant.ErrorInfo) {
                this.sawsDebugLog.write("Error reading certificate from keystore file when creating PKC request: " + e.getMessage());
            }
            return false;
        }
        PKCS10CertificationRequest request = null;
        PublicKey pk = cert.getPublicKey();
        try {
            request = new PKCS10CertificationRequest("SHA1with" + pk.getAlgorithm(), new X500Principal(((X509Certificate) cert).getSubjectDN().toString()), pk, new DERSet(), (PrivateKey) ks.getKey("saws", pw.toCharArray()));
            PEMWriter pemWrt = new PEMWriter(new OutputStreamWriter(new FileOutputStream("sawsRequest.csr")));
            pemWrt.writeObject(request);
            pemWrt.close();
            created = true;
        } catch (Exception e) {
            if (this.debugLevel >= SAWSConstant.ErrorInfo) {
                this.sawsDebugLog.write("Error creating PKC request file: " + e.getMessage());
            }
            return false;
        }
        return created;
    }
