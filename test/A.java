public long checksum(File f) throws IOException {
        CRC32 crc = new CRC32();
        FileReader fr = new FileReader(f);
        int data;
        while((data = fr.read()) != -1) {
        crc.update(data);
        }
        fr.close();
        return crc.getValue();
}