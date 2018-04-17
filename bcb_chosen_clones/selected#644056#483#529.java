    public static byte[] getHash(ChannelBuffer buffer, DigestAlgo algo) throws IOException {
        Checksum checksum = null;
        byte[] bytes = null;
        switch(algo) {
            case ADLER32:
                checksum = new Adler32();
            case CRC32:
                if (checksum == null) {
                    checksum = new CRC32();
                }
                bytes = new byte[buffer.readableBytes()];
                buffer.getBytes(buffer.readerIndex(), bytes);
                checksum.update(bytes, 0, bytes.length);
                bytes = null;
                bytes = Long.toOctalString(checksum.getValue()).getBytes();
                checksum = null;
                return bytes;
            case MD5:
                if (useFastMd5) {
                    MD5 md5 = new MD5();
                    md5.Update(buffer);
                    bytes = md5.Final();
                    md5 = null;
                    return bytes;
                }
            case MD2:
            case SHA1:
            case SHA256:
            case SHA384:
            case SHA512:
                String algoname = algo.name;
                bytes = new byte[buffer.readableBytes()];
                buffer.getBytes(buffer.readerIndex(), bytes);
                MessageDigest digest = null;
                try {
                    digest = MessageDigest.getInstance(algoname);
                } catch (NoSuchAlgorithmException e) {
                    throw new IOException(algoname + " Algorithm not supported by this JVM", e);
                }
                digest.update(bytes, 0, bytes.length);
                bytes = digest.digest();
                digest = null;
                return bytes;
            default:
                throw new IOException(algo.name + " Algorithm not supported by this JVM");
        }
    }
