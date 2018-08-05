    public static byte[] compress(byte[] whatToCompress, int length) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream gzos = new ZipOutputStream(baos);
        gzos.setMethod(ZipOutputStream.DEFLATED);
        gzos.putNextEntry(new ZipEntry(length + ""));
        gzos.write(whatToCompress, 0, length);
        gzos.closeEntry();
        gzos.finish();
        gzos.close();
        return baos.toByteArray();
    }
