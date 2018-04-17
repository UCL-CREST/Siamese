    public static byte[] expandPasswordToKey(String password, int keyLen, byte[] salt) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int digLen = md5.getDigestLength();
            byte[] mdBuf = new byte[digLen];
            byte[] key = new byte[keyLen];
            int cnt = 0;
            while (cnt < keyLen) {
                if (cnt > 0) {
                    md5.update(mdBuf);
                }
                md5.update(password.getBytes());
                md5.update(salt);
                md5.digest(mdBuf, 0, digLen);
                int n = ((digLen > (keyLen - cnt)) ? keyLen - cnt : digLen);
                System.arraycopy(mdBuf, 0, key, cnt, n);
                cnt += n;
            }
            return key;
        } catch (Exception e) {
            throw new Error("Error in SSH2KeyPairFile.expandPasswordToKey: " + e);
        }
    }
