    protected int writeEntry(ZipOutputStream jar, Filelike entry, ULog log) throws IOException, ValidationException {
        String archiveName = getNameStrategy().getArchiveName(log, entry);
        if (archiveName != null && archiveName.length() > 0) {
            ZipEntry ze = new ZipEntry(archiveName);
            log.fine("writing entry {0} ({1} bytes)", new Quote(ze.getName()), SIZE_FORMAT.format(entry.getSize()));
            byte[] bytes = entry.readWhole();
            if (!compressed) {
                ze.setMethod(ZipEntry.STORED);
                ze.setSize(entry.getSize());
                ze.setCompressedSize(entry.getSize());
                CRC32 crc = new CRC32();
                crc.update(bytes);
                ze.setCrc(crc.getValue());
            }
            jar.putNextEntry(ze);
            jar.write(bytes);
            jar.closeEntry();
            return bytes.length;
        } else {
            return 0;
        }
    }
