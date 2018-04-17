    public static byte[] compress(byte[] data) {
        ZipEntry ze = new ZipEntry("");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        zos.setLevel(9);
        try {
            zos.putNextEntry(ze);
            IOUtils.write(data, zos);
        } catch (IOException e) {
            throw new IORuntimeException("", e);
        }
        return baos.toByteArray();
    }
