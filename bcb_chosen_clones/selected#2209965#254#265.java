    private void addStream(ZipEntry pzipEntry, InputStream pfileInput) throws Exception {
        if (getZipArchive() == null) {
            this.zipArchive = new ZipOutputStream(new FileOutputStream(getZipFile()));
        }
        getZipArchive().putNextEntry(pzipEntry);
        int llngLength = pfileInput.read(getByteBuffer());
        while (llngLength > 0) {
            getZipArchive().write(getByteBuffer(), 0, llngLength);
            llngLength = pfileInput.read(getByteBuffer());
        }
        getZipArchive().closeEntry();
    }
