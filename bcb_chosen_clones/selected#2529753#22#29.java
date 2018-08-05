    public static final byte[] pack(byte[] bytes) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
        ZipOutputStream zos = new ZipOutputStream(baos);
        zos.putNextEntry(new ZipEntry(""));
        zos.write(bytes);
        zos.close();
        return baos.toByteArray();
    }
