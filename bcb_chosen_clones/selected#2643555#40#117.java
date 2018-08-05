    @Override
    public void run() {
        File file;
        try {
            file = new File(filePath);
            if (!file.canWrite()) {
                Thread.sleep(5000);
                if (!file.canWrite()) {
                    logger.error("Filed to gain write access to file:" + filePath);
                    exitState = false;
                    return;
                }
            }
            fis = new BufferedInputStream(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            logger.error("File not found:" + filePath);
            exitState = false;
            return;
        } catch (java.lang.InterruptedException ie) {
            logger.error("Upload thread halted or interrupted on file:" + filePath);
            exitState = false;
            return;
        }
        ftp = new FTPClient();
        ftp.setDefaultTimeout(20 * 1000);
        boolean uploadSuccessful = false;
        try {
            ftp.enterLocalPassiveMode();
            ftp.connect(ftpHostname);
            ftp.login(username, password);
            logger.trace("FTP Logged In and connected");
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                exitState = false;
                return;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            logger.trace("FTP Timeout Set and File Type Set");
            ftp.changeWorkingDirectory(serverDirectory);
            ftp.storeUniqueFile(file.getName(), fis);
            logger.trace("File Uploaded");
            if (FTPReply.isPositiveIntermediate(ftp.getReplyCode())) {
                System.out.println("Completing pending command");
                ftp.completePendingCommand();
            }
            uploadSuccessful = true;
        } catch (Exception e) {
            try {
                ftp.disconnect();
            } catch (Exception de) {
                e.printStackTrace();
            }
            logger.error("Error uploading file:", e);
        } finally {
            try {
                fis.close();
                logger.trace("File closed");
                ftp.logout();
                logger.trace("FTP Logged Out");
                ftp.disconnect();
                logger.trace("FTP Disconnected");
                if (uploadSuccessful) {
                    logger.info("Deleting file:" + new File(filePath).getName());
                    if (!(new File(filePath)).delete()) {
                        logger.error("Can't delete file for some reason");
                    }
                    Thread.sleep(1000);
                    exitState = true;
                    return;
                }
            } catch (Exception e) {
                logger.error("Exception on file upload cleanup:", e);
                exitState = false;
                return;
            }
        }
        exitState = false;
        return;
    }
