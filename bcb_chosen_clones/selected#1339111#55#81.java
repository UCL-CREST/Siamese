    public String generateDigest(String password, String saltHex, String algorithm) throws NoSuchAlgorithmException {
        if (algorithm.equalsIgnoreCase("crypt")) {
            return "{CRYPT}" + UnixCrypt.crypt(password);
        } else if (algorithm.equalsIgnoreCase("sha")) {
            algorithm = "SHA-1";
        } else if (algorithm.equalsIgnoreCase("md5")) {
            algorithm = "MD5";
        }
        MessageDigest msgDigest = MessageDigest.getInstance(algorithm);
        byte[] salt = {};
        if (saltHex != null) {
            salt = fromHex(saltHex);
        }
        String label = null;
        if (algorithm.startsWith("SHA")) {
            label = (salt.length > 0) ? "{SSHA}" : "{SHA}";
        } else if (algorithm.startsWith("MD5")) {
            label = (salt.length > 0) ? "{SMD5}" : "{MD5}";
        }
        msgDigest.reset();
        msgDigest.update(password.getBytes());
        msgDigest.update(salt);
        byte[] pwhash = msgDigest.digest();
        StringBuffer digest = new StringBuffer(label);
        digest.append(Base64.encode(concatenate(pwhash, salt)));
        return digest.toString();
    }
