    public CertificateID(String hashAlgorithm, X509Certificate issuerCert, BigInteger number, String provider) throws OCSPException {
        try {
            MessageDigest digest = MessageDigest.getInstance(hashAlgorithm, provider);
            AlgorithmIdentifier hashAlg = new AlgorithmIdentifier(new DERObjectIdentifier(hashAlgorithm), new DERNull());
            X509Principal issuerName = PrincipalUtil.getSubjectX509Principal(issuerCert);
            digest.update(issuerName.getEncoded());
            ASN1OctetString issuerNameHash = new DEROctetString(digest.digest());
            PublicKey issuerKey = issuerCert.getPublicKey();
            ASN1InputStream aIn = new ASN1InputStream(issuerKey.getEncoded());
            SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(aIn.readObject());
            digest.update(info.getPublicKeyData().getBytes());
            ASN1OctetString issuerKeyHash = new DEROctetString(digest.digest());
            DERInteger serialNumber = new DERInteger(number);
            this.id = new CertID(hashAlg, issuerNameHash, issuerKeyHash, serialNumber);
        } catch (Exception e) {
            throw new OCSPException("problem creating ID: " + e, e);
        }
    }
