    public String calcMD5(String sequence) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(sequence.toString().toUpperCase().getBytes());
        BigInteger md5hash = new BigInteger(1, md5.digest());
        String sequence_md5 = md5hash.toString(16);
        while (sequence_md5.length() < 32) {
            sequence_md5 = "0" + sequence_md5;
        }
        return sequence_md5;
    }
