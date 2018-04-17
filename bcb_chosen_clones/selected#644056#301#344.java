    private static byte[] getHashNoNio(InputStream in, DigestAlgo algo, byte[] buf) throws IOException {
        Checksum checksum = null;
        int size = 0;
        switch(algo) {
            case ADLER32:
                checksum = new Adler32();
            case CRC32:
                if (checksum == null) {
                    checksum = new CRC32();
                }
                while ((size = in.read(buf)) >= 0) {
                    checksum.update(buf, 0, size);
                }
                in.close();
                buf = null;
                buf = Long.toOctalString(checksum.getValue()).getBytes();
                checksum = null;
                break;
            case MD5:
            case MD2:
            case SHA1:
            case SHA256:
            case SHA384:
            case SHA512:
                String algoname = algo.name;
                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance(algoname);
                } catch (NoSuchAlgorithmException e) {
                    throw new IOException(algo + " Algorithm not supported by this JVM", e);
                }
                while ((size = in.read(buf)) >= 0) {
                    digest.update(buf, 0, size);
                }
                in.close();
                buf = null;
                buf = digest.digest();
                digest = null;
                break;
            default:
                throw new IOException(algo.name + " Algorithm not supported by this JVM");
        }
        return buf;
    }
