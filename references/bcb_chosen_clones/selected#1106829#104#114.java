    public long checksumFile(String name) throws IOException {
        byte[] buffer = new byte[4000];
        InputStream in = new FileInputStream(new File(dir, name));
        Checksum cksum = new CRC32();
        while (true) {
            int n = in.read(buffer);
            if (n < 0) break;
            cksum.update(buffer, 0, n);
        }
        return cksum.getValue();
    }
