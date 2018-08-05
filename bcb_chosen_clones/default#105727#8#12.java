    public static byte[] generateHash(String ValueToHash, String Algo) throws NoSuchAlgorithmException {
        MessageDigest Hash = MessageDigest.getInstance(Algo);
        Hash.update(ValueToHash.getBytes());
        return Hash.digest();
    }
