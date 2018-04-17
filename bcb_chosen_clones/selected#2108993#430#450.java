    static long computeFileCrc(File f) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            CRC32 crc = new CRC32();
            byte[] data = new byte[BUFFER_SIZE];
            int len = -1;
            while ((len = fis.read(data)) > -1) {
                crc.update(data, 0, len);
            }
            return crc.getValue();
        } catch (IOException e) {
            logger.error("Unexpected exception while computing CRC on file: " + f.getAbsolutePath(), e);
            return -1;
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
            }
        }
    }
