    private void zip(FileHolder fileHolder, int zipCompressionLevel) {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes_read;
        if (fileHolder.selectedFileList.size() == 0) {
            return;
        }
        File zipDestFile = new File(fileHolder.destFiles[0]);
        try {
            ZipOutputStream outStream = new ZipOutputStream(new FileOutputStream(zipDestFile));
            for (int i = 0; i < fileHolder.selectedFileList.size(); i++) {
                File selectedFile = fileHolder.selectedFileList.get(i);
                super.currentObjBeingProcessed = selectedFile;
                this.inStream = new FileInputStream(selectedFile);
                ZipEntry entry = new ZipEntry(selectedFile.getName());
                outStream.setLevel(zipCompressionLevel);
                outStream.putNextEntry(entry);
                while ((bytes_read = this.inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytes_read);
                }
                outStream.closeEntry();
                this.inStream.close();
            }
            outStream.close();
        } catch (IOException e) {
            errEntry.setThrowable(e);
            errEntry.setAppContext("gzip()");
            errEntry.setAppMessage("Error zipping: " + zipDestFile);
            logger.logError(errEntry);
        }
        return;
    }
