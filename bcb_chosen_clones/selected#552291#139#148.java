    private long getCRCValue(File file) throws IOException {
        CRC32 crc = new CRC32();
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        int b;
        while ((b = input.read()) != -1) {
            crc.update(b);
        }
        input.close();
        return crc.getValue();
    }
