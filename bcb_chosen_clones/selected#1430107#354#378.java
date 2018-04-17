    char[] DigestCalcHA1(String algorithm, String userName, String realm, String password, String nonce, String clientNonce) throws SaslException {
        byte[] hash;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(userName.getBytes("UTF-8"));
            md.update(":".getBytes("UTF-8"));
            md.update(realm.getBytes("UTF-8"));
            md.update(":".getBytes("UTF-8"));
            md.update(password.getBytes("UTF-8"));
            hash = md.digest();
            if ("md5-sess".equals(algorithm)) {
                md.update(hash);
                md.update(":".getBytes("UTF-8"));
                md.update(nonce.getBytes("UTF-8"));
                md.update(":".getBytes("UTF-8"));
                md.update(clientNonce.getBytes("UTF-8"));
                hash = md.digest();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new SaslException("No provider found for MD5 hash", e);
        } catch (UnsupportedEncodingException e) {
            throw new SaslException("UTF-8 encoding not supported by platform.", e);
        }
        return convertToHex(hash);
    }
