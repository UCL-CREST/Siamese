    private void writeZipEntry(ZipOutputStream zos, ZipEntry zipEntry, InputStream is) throws IOException {
        zos.putNextEntry(zipEntry);
        byte[] buf = new byte[1024];
        int in;
        while ((in = is.read(buf, 0, buf.length)) > 0) zos.write(buf, 0, in);
        zos.closeEntry();
    }
