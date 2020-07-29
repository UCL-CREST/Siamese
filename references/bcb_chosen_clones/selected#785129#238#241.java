    private void addFileToZip(String filename, ZipOutputStream out) throws IOException {
        out.putNextEntry(new ZipEntry(filename));
        out.closeEntry();
    }
