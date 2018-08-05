    public void addNonKMLFile(final String filename, final String inZipFilename) throws IOException {
        if (this.nonKmlFiles.containsKey(filename) && (inZipFilename.compareTo(this.nonKmlFiles.get(filename)) == 0)) {
            log.warn("File: " + filename + " is already included in the kmz as " + inZipFilename);
            return;
        }
        this.nonKmlFiles.put(filename, inZipFilename);
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(filename);
            byte[] buffer = new byte[1024];
            int bytesRead;
            ZipEntry entry = new ZipEntry(inZipFilename);
            this.zipOut.putNextEntry(entry);
            while ((bytesRead = inStream.read(buffer)) != -1) {
                this.zipOut.write(buffer, 0, bytesRead);
            }
            log.info(entry.getName() + " added to kmz.");
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }
    }
