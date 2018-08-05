    public String getDigest(String algorithm, String data) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = java.security.MessageDigest.getInstance(algorithm);
        md.reset();
        md.update(data.getBytes());
        return md.digest().toString();
    }
