    public void addNonKMLFile(final byte[] data, final String inZipFilename) throws IOException {
        ZipEntry entry = new ZipEntry(inZipFilename);
        this.zipOut.putNextEntry(entry);
        this.zipOut.write(data);
        log.debug(entry.getName() + " added to kmz.");
    }
