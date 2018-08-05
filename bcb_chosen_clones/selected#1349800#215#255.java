    public static long sampledCRC(File file) throws FileNotFoundException {
        long fileSize = file.length();
        RandomAccessFile fs = new RandomAccessFile(file, "r");
        long startTime = System.currentTimeMillis();
        CRC32 crc = new CRC32();
        for (int i = 7; i >= 0; i--) {
            crc.update((int) (fileSize >> (8 * i)));
        }
        final int CHUNK_SIZE = 4096;
        final int MINIMUM_CHUNK_SKIP = 1;
        final int MAXIMUM_CHUNK_SKIP = 1000;
        final double CHUNK_SKIP_MULTIPLIER = 1.2;
        int totalRead = 0;
        byte[] data = new byte[CHUNK_SIZE];
        for (int position = 0, chunkSkip = 0; ; ) {
            try {
                fs.seek(position);
                int read = fs.read(data, 0, data.length);
                if (read == -1) {
                    break;
                }
                position += read + chunkSkip * CHUNK_SIZE;
                if (position > 64 << 10) {
                    if (chunkSkip == 0) {
                        chunkSkip = MINIMUM_CHUNK_SKIP;
                    } else {
                        chunkSkip = Math.min((int) Math.ceil(chunkSkip * CHUNK_SKIP_MULTIPLIER), MAXIMUM_CHUNK_SKIP);
                    }
                }
                totalRead += read;
                crc.update(data, 0, read);
            } catch (IOException ioe) {
            }
            if ((totalRead % (100 * CHUNK_SIZE)) == 0) {
                Logging.verboseln(1, "Processed " + (totalRead / 1024) + "KBytes.  CRC32 = " + crc.getValue());
            }
        }
        long endTime = System.currentTimeMillis();
        Logging.msgln("Sampled " + (totalRead / 1024) + " of " + (fileSize / 1024) + " KBytes. CRC took " + (endTime - startTime) + "ms.  CRC32 = " + crc.getValue());
        return crc.getValue();
    }
