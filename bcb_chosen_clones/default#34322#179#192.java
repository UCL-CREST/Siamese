    public void createVendorSignature() {
        byte b;
        try {
            _vendorMessageDigest = MessageDigest.getInstance("MD5");
            _vendorSig = Signature.getInstance("MD5/RSA/PKCS#1");
            _vendorSig.initSign((PrivateKey) _vendorPrivateKey);
            _vendorMessageDigest.update(getBankString().getBytes());
            _vendorMessageDigestBytes = _vendorMessageDigest.digest();
            _vendorSig.update(_vendorMessageDigestBytes);
            _vendorSignatureBytes = _vendorSig.sign();
        } catch (Exception e) {
        }
        ;
    }
