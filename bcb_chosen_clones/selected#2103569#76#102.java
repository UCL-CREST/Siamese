    public void writeHome(Home home) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(this.out);
        zipOut.setLevel(this.compressionLevel);
        checkCurrentThreadIsntInterrupted();
        zipOut.putNextEntry(new ZipEntry("Home"));
        ObjectOutputStream objectOut = new HomeObjectOutputStream(zipOut);
        objectOut.writeObject(home);
        objectOut.flush();
        zipOut.closeEntry();
        for (int i = 0, n = contents.size(); i < n; i++) {
            Content content = contents.get(i);
            String entryNameOrDirectory = String.valueOf(i);
            if (content instanceof ResourceURLContent) {
                writeResourceZipEntries(zipOut, entryNameOrDirectory, (ResourceURLContent) content);
            } else if (content instanceof URLContent && ((URLContent) content).isJAREntry()) {
                URLContent urlContent = (URLContent) content;
                if (urlContent instanceof HomeURLContent) {
                    writeHomeZipEntries(zipOut, entryNameOrDirectory, (HomeURLContent) urlContent);
                } else {
                    writeZipEntries(zipOut, entryNameOrDirectory, urlContent);
                }
            } else {
                writeZipEntry(zipOut, entryNameOrDirectory, content);
            }
        }
        zipOut.finish();
    }
