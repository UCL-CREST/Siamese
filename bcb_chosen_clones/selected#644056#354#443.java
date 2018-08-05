    public static byte[] getHash(File f, boolean nio, DigestAlgo algo) throws IOException {
        if (!f.exists()) {
            throw new FileNotFoundException(f.toString());
        }
        if (algo == DigestAlgo.MD5 && useFastMd5) {
            if (nio) {
                return MD5.getHashNio(f);
            } else {
                return MD5.getHash(f);
            }
        }
        InputStream close_me = null;
        try {
            long buf_size = f.length();
            if (buf_size < 512) {
                buf_size = 512;
            }
            if (buf_size > 65536) {
                buf_size = 65536;
            }
            byte[] buf = new byte[(int) buf_size];
            FileInputStream in = new FileInputStream(f);
            close_me = in;
            if (nio) {
                FileChannel fileChannel = in.getChannel();
                ByteBuffer bb = ByteBuffer.wrap(buf);
                Checksum checksum = null;
                int size = 0;
                switch(algo) {
                    case ADLER32:
                        checksum = new Adler32();
                    case CRC32:
                        if (checksum == null) {
                            checksum = new CRC32();
                        }
                        while ((size = fileChannel.read(bb)) >= 0) {
                            checksum.update(buf, 0, size);
                            bb.clear();
                        }
                        fileChannel.close();
                        fileChannel = null;
                        bb = null;
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
                        while ((size = fileChannel.read(bb)) >= 0) {
                            digest.update(buf, 0, size);
                            bb.clear();
                        }
                        fileChannel.close();
                        fileChannel = null;
                        bb = null;
                        buf = digest.digest();
                        digest = null;
                        break;
                    default:
                        throw new IOException(algo.name + " Algorithm not supported by this JVM");
                }
            } else {
                buf = getHashNoNio(in, algo, buf);
                in = null;
                close_me = null;
                return buf;
            }
            in = null;
            close_me = null;
            return buf;
        } catch (IOException e) {
            if (close_me != null) {
                try {
                    close_me.close();
                } catch (Exception e2) {
                }
            }
            throw e;
        }
    }
