    public static byte[] expandPasswordToKeySSHCom(String password, int keyLen) {
        try {
            if (password == null) {
                password = "";
            }
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int digLen = md5.getDigestLength();
            byte[] buf = new byte[((keyLen + digLen) / digLen) * digLen];
            int cnt = 0;
            while (cnt < keyLen) {
                md5.update(password.getBytes());
                if (cnt > 0) {
                    md5.update(buf, 0, cnt);
                }
                md5.digest(buf, cnt, digLen);
                cnt += digLen;
            }
            byte[] key = new byte[keyLen];
            System.arraycopy(buf, 0, key, 0, keyLen);
            return key;
        } catch (Exception e) {
            throw new Error("Error in SSH2KeyPairFile.expandPasswordToKeySSHCom: " + e);
        }
    }
