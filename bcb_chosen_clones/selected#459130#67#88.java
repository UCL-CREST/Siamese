    private void handleDir(File dir, ZipOutputStream zipOut) throws IOException {
        FileInputStream fileIn;
        File[] files;
        files = dir.listFiles();
        if (files.length == 0) {
            this.zipOut.putNextEntry(new ZipEntry(dir.toString() + "/"));
            this.zipOut.closeEntry();
        } else {
            for (File fileName : files) {
                if (fileName.isDirectory()) {
                    handleDir(fileName, this.zipOut);
                } else {
                    fileIn = new FileInputStream(fileName);
                    this.zipOut.putNextEntry(new ZipEntry(fileName.toString()));
                    while ((this.readedBytes = fileIn.read(this.buf)) > 0) {
                        this.zipOut.write(this.buf, 0, this.readedBytes);
                    }
                    this.zipOut.closeEntry();
                }
            }
        }
    }
