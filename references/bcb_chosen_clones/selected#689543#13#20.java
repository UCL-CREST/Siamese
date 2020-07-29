    public static byte[] ComputeForBinary(String ThisString) throws Exception {
        byte[] Result;
        MessageDigest MD5Hasher;
        MD5Hasher = MessageDigest.getInstance("MD5");
        MD5Hasher.update(ThisString.getBytes("iso-8859-1"));
        Result = MD5Hasher.digest();
        return Result;
    }
