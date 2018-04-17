    private byte[] digestFile(File file, MessageDigest digest) throws IOException {
        DigestInputStream in = new DigestInputStream(new FileInputStream(file), digest);
        IOUtils.copy(in, new NullOutputStream());
        in.close();
        return in.getMessageDigest().digest();
    }
