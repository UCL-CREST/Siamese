    public BigInteger calculateMd5(String input) throws FileSystemException {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            byte[] messageDigest = digest.digest();
            BigInteger bigInt = new BigInteger(1, messageDigest);
            return bigInt;
        } catch (Exception e) {
            throw new FileSystemException(e);
        }
    }
