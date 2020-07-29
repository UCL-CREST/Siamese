    private long calcCRC32(File file) throws IOException {
        CRC32 crc = new CRC32();
        byte[] buf = new byte[2048];
        int read;
        InputStream is = new FileInputStream(file);
        while ((read = is.read(buf, 0, buf.length)) > -1) {
            crc.update(buf, 0, read);
        }
        is.close();
        return crc.getValue();
    }
