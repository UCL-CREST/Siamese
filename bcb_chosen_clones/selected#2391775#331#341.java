    private ZipEntry createZipEntry(String name, byte bytes[], int method) {
        ZipEntry ze = new ZipEntry(name);
        ze.setMethod(method);
        ze.setSize(bytes.length);
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(bytes);
        ze.setCrc(crc.getValue());
        ze.setTime(System.currentTimeMillis());
        return ze;
    }
