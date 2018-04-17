    private long copyBackupToFile(BackupDataInput data, File file, int toRead) throws IOException {
        final int CHUNK = 8192;
        byte[] buf = new byte[CHUNK];
        CRC32 crc = new CRC32();
        FileOutputStream out = new FileOutputStream(file);
        try {
            while (toRead > 0) {
                int numRead = data.readEntityData(buf, 0, CHUNK);
                crc.update(buf, 0, numRead);
                out.write(buf, 0, numRead);
                toRead -= numRead;
            }
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return crc.getValue();
    }
