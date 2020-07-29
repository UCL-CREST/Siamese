    public static long checksum(File file) throws java.io.IOException, FileNotFoundException {
        FileInputStream fis = null;
        byte[] bytes = new byte[16384];
        int len;
        try {
            fis = new FileInputStream(file);
            CRC32 chkSum = new CRC32();
            len = fis.read(bytes);
            while (len != -1) {
                chkSum.update(bytes, 0, len);
                len = fis.read(bytes);
            }
            return chkSum.getValue();
        } finally {
            quietClose(fis);
        }
    }
