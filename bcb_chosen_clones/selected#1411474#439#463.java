    static void writeZipEntry(final byte[] data, final ZipOutputStream out, final ZipEntry entry, final boolean isCopy) throws IOException {
        if (isCopy) {
            out.putNextEntry(entry);
            try {
                out.write(data);
            } finally {
                out.closeEntry();
            }
        } else {
            final ZipEntry entryCopy = new ZipEntry(entry.getName());
            entryCopy.setTime(entry.getTime());
            entryCopy.setMethod(ZipOutputStream.STORED);
            entryCopy.setSize(data.length);
            entryCopy.setCompressedSize(data.length);
            final CRC32 crc = new CRC32();
            crc.update(data);
            entryCopy.setCrc(crc.getValue());
            out.putNextEntry(entryCopy);
            try {
                out.write(data);
            } finally {
                out.closeEntry();
            }
        }
    }
