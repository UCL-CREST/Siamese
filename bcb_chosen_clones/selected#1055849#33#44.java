    public RespID(PublicKey key) throws OCSPException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            ASN1InputStream aIn = new ASN1InputStream(key.getEncoded());
            SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(aIn.readObject());
            digest.update(info.getPublicKeyData().getBytes());
            ASN1OctetString keyHash = new DEROctetString(digest.digest());
            this.id = new ResponderID(keyHash);
        } catch (Exception e) {
            throw new OCSPException("problem creating ID: " + e, e);
        }
    }
