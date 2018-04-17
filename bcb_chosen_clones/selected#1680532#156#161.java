    public void addNonKMLFile(final byte[] file, final String inZipFilename) throws IOException {
        ZipEntry entry = new ZipEntry(inZipFilename);
        this.zipOut.putNextEntry(entry);
        this.zipOut.write(file);
        log.info(entry.getName() + " added to kmz.");
    }
