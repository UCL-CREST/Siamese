    public void zip(final String sourceDirectory, OutputStream dest) throws FileNotFoundException, IOException {
        logger.doLog(AppLogger.DEBUG, "Starting compression...", null);
        this.sourceDirectory = sourceDirectory;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        fileList = new ArrayList<String>();
        fileListPopulated = false;
        boolean processingComplete = false;
        Thread thread = new Thread(this);
        thread.start();
        Object[] files = null;
        while (!processingComplete) {
            synchronized (fileList) {
                if (fileList.size() == 0) {
                    logger.doLog(AppLogger.DEBUG, "Sleeping for 1 second", null);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        logger.doLog(AppLogger.DEBUG, "Thread interrupted. Resuming.", null);
                    }
                } else {
                    processingComplete = fileListPopulated;
                    files = fileList.toArray();
                    fileList.clear();
                }
            }
            if (files != null && files.length > 0) {
                logger.doLog(AppLogger.DEBUG, "Compressing " + files.length + " files (and empty directories)", null);
                byte[] tmpBuf = new byte[BUFFER];
                for (int i = 0; i < files.length; i++) {
                    String fullFileName = (String) files[i];
                    ZipEntry entry = new ZipEntry(fullFileName.substring(new File(sourceDirectory).getAbsolutePath().length()));
                    out.putNextEntry(entry);
                    if (new File(fullFileName).isFile()) {
                        jobListener.updateSubStatus("Compressing " + fullFileName + "...");
                        FileInputStream in = new FileInputStream(fullFileName);
                        int len;
                        try {
                            while ((len = in.read(tmpBuf)) > 0) {
                                out.write(tmpBuf, 0, len);
                            }
                        } catch (IOException e) {
                            logger.doLog(AppLogger.WARN, "Exception while zipping " + fullFileName, e);
                            if ("The process cannot access the file because another process has locked a portion of the file".equals(e.getMessage())) {
                                logger.doLog(AppLogger.INFO, "Another process has locked this file for modification. If the file is updated while it is being compressed, the entire archive may be corrupt." + "\nContinuing.", null);
                            }
                        }
                        in.close();
                    }
                    out.closeEntry();
                }
                out.flush();
                files = null;
            }
        }
        out.close();
        logger.doLog(AppLogger.DEBUG, "Compression complete.", null);
        jobListener.updateMainStatus("Compression complete.");
    }
