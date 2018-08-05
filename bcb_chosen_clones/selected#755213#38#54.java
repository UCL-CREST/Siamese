    public ODFSignatureService(TimeStampServiceValidator timeStampServiceValidator, RevocationDataService revocationDataService, SignatureFacet signatureFacet, InputStream documentInputStream, OutputStream documentOutputStream, TimeStampService timeStampService, String role, IdentityDTO identity, byte[] photo, DigestAlgo digestAlgo) throws Exception {
        super(digestAlgo);
        this.temporaryDataStorage = new HttpSessionTemporaryDataStorage();
        this.documentOutputStream = documentOutputStream;
        this.tmpFile = File.createTempFile("eid-dss-", ".odf");
        FileOutputStream fileOutputStream;
        fileOutputStream = new FileOutputStream(this.tmpFile);
        IOUtils.copy(documentInputStream, fileOutputStream);
        addSignatureFacet(new XAdESXLSignatureFacet(timeStampService, revocationDataService, getSignatureDigestAlgorithm()));
        addSignatureFacet(signatureFacet);
        XAdESSignatureFacet xadesSignatureFacet = super.getXAdESSignatureFacet();
        xadesSignatureFacet.setRole(role);
        if (null != identity) {
            IdentitySignatureFacet identitySignatureFacet = new IdentitySignatureFacet(identity, photo, getSignatureDigestAlgorithm());
            addSignatureFacet(identitySignatureFacet);
        }
    }
