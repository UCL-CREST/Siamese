    private String md5Digest(String plain) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(plain.trim().getBytes());
        byte pwdDigest[] = digest.digest();
        StringBuilder md5buffer = new StringBuilder();
        for (int i = 0; i < pwdDigest.length; i++) {
            int number = 0xFF & pwdDigest[i];
            if (number <= 0xF) {
                md5buffer.append('0');
            }
            md5buffer.append(Integer.toHexString(number));
        }
        return md5buffer.toString();
    }
