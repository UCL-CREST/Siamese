    public void retrieveFiles() throws DataSyncException {
        try {
            ftp.connect(hostname, port);
            boolean success = ftp.login(username, password);
            log.info("FTP Login:" + success);
            if (success) {
                System.out.println(directory);
                ftp.changeWorkingDirectory(directory);
                ftp.setFileType(FTP.ASCII_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                ftp.setRemoteVerificationEnabled(false);
                FTPFile[] files = ftp.listFiles();
                for (FTPFile file : files) {
                    ftp.setFileType(file.getType());
                    log.debug(file.getName() + "," + file.getSize());
                    FileOutputStream output = new FileOutputStream(localDirectory + file.getName());
                    try {
                        ftp.retrieveFile(file.getName(), output);
                    } finally {
                        IOUtils.closeQuietly(output);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataSyncException(e);
        } finally {
            try {
                ftp.disconnect();
            } catch (IOException e) {
            }
        }
    }
