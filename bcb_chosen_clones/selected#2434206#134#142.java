    private static void modifyZipEntry(ZipEntry entry, byte[] bytes, CRC32 crc32) {
        entry.setSize(bytes.length);
        if (entry.getMethod() == 0) {
            crc32.reset();
            crc32.update(bytes);
            entry.setCrc(crc32.getValue());
            entry.setCompressedSize(bytes.length);
        }
    }
