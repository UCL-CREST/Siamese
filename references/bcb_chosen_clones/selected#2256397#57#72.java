    private static byte[] compress(byte[] bytes, String name) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        CRC32 crc = new CRC32();
        ZipOutputStream zos = new ZipOutputStream(baos);
        zos.setMethod(ZipEntry.DEFLATED);
        zos.setLevel(6);
        ZipEntry ze = new ZipEntry(name);
        ze.setSize(bytes.length);
        crc.update(bytes);
        ze.setCrc(crc.getValue());
        zos.putNextEntry(ze);
        zos.write(bytes);
        zos.closeEntry();
        zos.close();
        return baos.toByteArray();
    }
