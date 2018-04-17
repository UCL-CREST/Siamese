    public static long getCrc(File file) throws Exception {
        CRC32 crc32 = new CRC32();
        FileInputStream fileinputstream = new FileInputStream(file);
        int i = 50000;
        byte buffer[] = new byte[50000];
        long l = file.length();
        int j = (int) l;
        int k = j;
        if (k > 50000) {
            k = 50000;
        }
        for (int j1 = read(fileinputstream, buffer, k); j1 > 0; ) {
            crc32.update(buffer, 0, j1);
            int k1 = (int) (((l - (long) j) * 100L) / l);
            j -= j1;
            int i1 = j;
            if (i1 > 50000) {
                i1 = 50000;
            }
            j1 = read(fileinputstream, buffer, i1);
            Thread.yield();
        }
        fileinputstream.close();
        return crc32.getValue();
    }
