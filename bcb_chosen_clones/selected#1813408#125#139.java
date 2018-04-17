    public long getCheckSum(File f) throws IOException {
        CRC32 result = new CRC32();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            int len = 0;
            byte[] buffer = new byte[DISK_DATA_BUFFER_SIZE];
            while ((len = fis.read(buffer)) != -1) {
                result.update(buffer, 0, len);
            }
        } finally {
            closeInputStream(fis);
        }
        return result.getValue();
    }
