    public synchronized void zipNonCompressed(String name, byte[] in) throws IOException {
        final ZipEntry entry = new ZipEntry(name);
        entry.setMethod(ZipEntry.STORED);
        final CRC32 crc = new CRC32();
        crc.update(in);
        entry.setCrc(crc.getValue());
        entry.setSize(in.length);
        this.putNextEntry(entry);
        this.getOutStream().write(in);
        this.closeEntry();
    }
