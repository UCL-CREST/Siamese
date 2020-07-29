    public static long getCRC(File f) throws IOException {
        InputStream is = new FileInputStream(f);
        CRC32 crc = new CRC32();
        byte[] data = new byte[1024];
        int read;
        while ((read = is.read(data)) > -1) {
            crc.update(data, 0, read);
        }
        return crc.getValue();
    }
