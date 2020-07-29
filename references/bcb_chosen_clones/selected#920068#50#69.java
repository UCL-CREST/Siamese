    public AbstractASiCSignatureService(InputStream documentInputStream, DigestAlgo digestAlgo, RevocationDataService revocationDataService, TimeStampService timeStampService, String claimedRole, IdentityDTO identity, byte[] photo, TemporaryDataStorage temporaryDataStorage, OutputStream documentOutputStream) throws IOException {
        super(digestAlgo);
        this.temporaryDataStorage = temporaryDataStorage;
        this.documentOutputStream = documentOutputStream;
        this.tmpFile = File.createTempFile("eid-dss-", ".asice");
        FileOutputStream fileOutputStream;
        fileOutputStream = new FileOutputStream(this.tmpFile);
        IOUtils.copy(documentInputStream, fileOutputStream);
        addSignatureFacet(new ASiCSignatureFacet(this.tmpFile, digestAlgo));
        XAdESSignatureFacet xadesSignatureFacet = new XAdESSignatureFacet(getSignatureDigestAlgorithm());
        xadesSignatureFacet.setRole(claimedRole);
        xadesSignatureFacet.setXadesNamespacePrefix("xades");
        addSignatureFacet(xadesSignatureFacet);
        addSignatureFacet(new XAdESXLSignatureFacet(timeStampService, revocationDataService, getSignatureDigestAlgorithm()));
        addSignatureFacet(new KeyInfoSignatureFacet(true, false, false));
        if (null != identity) {
            IdentitySignatureFacet identitySignatureFacet = new IdentitySignatureFacet(identity, photo, getSignatureDigestAlgorithm());
            addSignatureFacet(identitySignatureFacet);
        }
    }
