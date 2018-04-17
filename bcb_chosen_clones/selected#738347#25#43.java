    public static void main(String[] args) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
            keyGen.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyGen.generateKeyPair();
            CAReferenceField caRef = new CAReferenceField("SE", "PASS-CVCA", "00111");
            HolderReferenceField holderRef = new HolderReferenceField(caRef.getCountry(), caRef.getMnemonic(), caRef.getSequence());
            CVCertificate cvc = CertificateGenerator.createTestCertificate(keyPair.getPublic(), keyPair.getPrivate(), caRef, holderRef, "SHA1WithRSA", new AuthorizationRole(AuthorizationRole.TRUST_ROOT, 0xF));
            byte[] certData = cvc.getDEREncoded();
            String filename = "C:/cv_certs/mycert1.cvcert";
            FileHelper.writeFile(new File(filename), certData);
            certData = FileHelper.loadFile(new File(filename));
            CVCObject parsedObject = CertificateParser.parseCertificate(certData);
            System.out.println(parsedObject.getAsText(""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
