    public void createBankSignature() {
        byte b;
        try {
            _bankMessageDigest = MessageDigest.getInstance("MD5");
            _bankSig = Signature.getInstance("MD5/RSA/PKCS#1");
            _bankSig.initSign((PrivateKey) _bankPrivateKey);
            _bankMessageDigest.update(getBankString().getBytes());
            _bankMessageDigestBytes = _bankMessageDigest.digest();
            _bankSig.update(_bankMessageDigestBytes);
            _bankSignatureBytes = _bankSig.sign();
        } catch (Exception e) {
        }
        ;
    }
