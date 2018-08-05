    public void addNonKMLFile(final InputStream data, final String inZipFilename) throws IOException {
        try {
            byte[] buffer = new byte[4096];
            int bytesRead;
            ZipEntry entry = new ZipEntry(inZipFilename);
            this.zipOut.putNextEntry(entry);
            while ((bytesRead = data.read(buffer)) != -1) {
                this.zipOut.write(buffer, 0, bytesRead);
            }
            log.debug(entry.getName() + " added to kmz.");
        } finally {
            data.close();
        }
    }
