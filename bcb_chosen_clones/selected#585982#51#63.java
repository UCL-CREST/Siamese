        public void close() throws IOException {
            byte[] bytes = buffer.toByteArray();
            ZipEntry entry = new ZipEntry(name);
            entry.setMethod(ZipOutputStream.STORED);
            entry.setSize(bytes.length);
            entry.setCompressedSize(bytes.length);
            CRC32 crc = new CRC32();
            crc.update(bytes);
            entry.setCrc(crc.getValue());
            zip.putNextEntry(entry);
            zip.write(bytes);
            zip.closeEntry();
        }
