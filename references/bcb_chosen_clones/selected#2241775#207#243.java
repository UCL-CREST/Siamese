    private void copyFile(File sourceFile, File targetFile) {
        beNice();
        dispatchEvent(SynchronizationEventType.FileCopy, sourceFile, targetFile);
        File temporaryFile = new File(targetFile.getPath().concat(".jnstemp"));
        while (temporaryFile.exists()) {
            try {
                beNice();
                temporaryFile.delete();
                beNice();
            } catch (Exception ex) {
            }
        }
        try {
            if (targetFile.exists()) {
                targetFile.delete();
            }
            FileInputStream fis = new FileInputStream(sourceFile);
            FileOutputStream fos = new FileOutputStream(temporaryFile);
            byte[] buffer = new byte[204800];
            int readBytes = 0;
            int counter = 0;
            while ((readBytes = fis.read(buffer)) != -1) {
                counter++;
                updateStatus("... processing fragment " + String.valueOf(counter));
                fos.write(buffer, 0, readBytes);
            }
            fis.close();
            fos.close();
            temporaryFile.renameTo(targetFile);
            temporaryFile.setLastModified(sourceFile.lastModified());
            targetFile.setLastModified(sourceFile.lastModified());
        } catch (IOException e) {
            Exception dispatchedException = new Exception("ERROR: Copy File( " + sourceFile.getPath() + ", " + targetFile.getPath() + " )");
            dispatchEvent(dispatchedException, sourceFile, targetFile);
        }
        dispatchEvent(SynchronizationEventType.FileCopyDone, sourceFile, targetFile);
    }
