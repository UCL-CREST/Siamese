    public static long getCRCFor(File file, long start, long end) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            byte[] buffer = new byte[1024];
            CRC32 crc = new CRC32();
            raf.seek(start);
            int numRead;
            do {
                if (raf.getFilePointer() >= (end - 1024)) {
                    buffer = new byte[(int) (end - raf.getFilePointer())];
                }
                numRead = raf.read(buffer);
                if (numRead > 0) {
                    crc.update(buffer);
                }
            } while (numRead > 0);
            raf.close();
            return crc.getValue();
        } catch (IOException err) {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception foo) {
                }
            }
            throw err;
        }
    }
