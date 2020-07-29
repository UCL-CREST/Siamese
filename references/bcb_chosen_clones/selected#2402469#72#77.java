    private void writeToZip(InputStream is, String entry, ZipOutputStream zos) throws java.io.IOException {
        zos.putNextEntry(new ZipEntry(entry));
        for (int iRead = is.read(); iRead != -1; iRead = is.read()) {
            zos.write(iRead);
        }
    }
