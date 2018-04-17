    private void tar(FileHolder fileHolder, boolean gzipIt) {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytes_read;
        if (fileHolder.selectedFileList.size() == 0) {
            return;
        }
        File tarDestFile = new File(fileHolder.destFiles[0]);
        try {
            OutputStream outStream = new FileOutputStream(tarDestFile);
            if (gzipIt) {
                outStream = new GZIPOutputStream(outStream);
            }
            TarOutputStream tarOutStream = new TarOutputStream(outStream);
            for (int i = 0; i < fileHolder.selectedFileList.size(); i++) {
                File selectedFile = fileHolder.selectedFileList.get(i);
                super.currentObjBeingProcessed = selectedFile;
                this.inStream = new FileInputStream(selectedFile);
                TarEntry tarEntry = null;
                try {
                    tarEntry = new TarEntry(selectedFile, selectedFile.getName());
                } catch (InvalidHeaderException e) {
                    errEntry.setThrowable(e);
                    errEntry.setAppContext("tar()");
                    errEntry.setAppMessage("Error tar'ing: " + selectedFile);
                    logger.logError(errEntry);
                }
                tarOutStream.putNextEntry(tarEntry);
                while ((bytes_read = inStream.read(buffer)) != -1) {
                    tarOutStream.write(buffer, 0, bytes_read);
                }
                tarOutStream.closeEntry();
                inStream.close();
                super.processorSyncFlag.restartWaitUntilFalse();
            }
            tarOutStream.close();
        } catch (Exception e) {
            errEntry.setThrowable(e);
            errEntry.setAppContext("tar()");
            errEntry.setAppMessage("Error tar'ing: " + tarDestFile);
            logger.logError(errEntry);
        }
    }
