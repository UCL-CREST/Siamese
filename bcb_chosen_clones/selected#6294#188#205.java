    private String generateHash(String key, String data) throws ChiropteraException {
        try {
            MessageDigest md = MessageDigest.getInstance(Constants.Connection.Auth.MD5);
            md.update(key.getBytes());
            byte[] raw = md.digest();
            String s = toHexString(raw);
            SecretKey skey = new SecretKeySpec(s.getBytes(), Constants.Connection.Auth.HMACMD5);
            Mac mac = Mac.getInstance(skey.getAlgorithm());
            mac.init(skey);
            byte digest[] = mac.doFinal(data.getBytes());
            String digestB64 = BaculaBase64.binToBase64(digest);
            return digestB64.substring(0, digestB64.length());
        } catch (NoSuchAlgorithmException e) {
            throw new ChiropteraException(Constants.Chiroptera.Errors.HASH, e.getMessage(), e);
        } catch (InvalidKeyException e) {
            throw new ChiropteraException(Constants.Chiroptera.Errors.HASH, e.getMessage(), e);
        }
    }
