    protected static void upload(FtpSiteConnector connector, File localFile, String remotePath, boolean createRootDir, IProgressMonitor monitor) throws FtpException {
        FTPClient ftp = new FTPClient();
        try {
            String hostname = connector.getUrl().getHost();
            ftp.connect(hostname);
            log.info("Connected to " + hostname);
            log.info(ftp.getReplyString());
            boolean loggedIn = ftp.login(connector.getUsername(), connector.getPassword());
            if (loggedIn) {
                log.info("User " + connector.getUsername() + " logged in");
                ftp.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
                ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftp.enterLocalPassiveMode();
                FtpUtils.store(ftp, localFile, remotePath, createRootDir, monitor);
                ftp.logout();
            } else {
                throw new FtpException("Invalid login");
            }
            ftp.disconnect();
        } catch (Exception e) {
            log.error("File upload failed with message: " + e.getMessage());
            throw new FtpException("File upload failed with message: " + e.getMessage());
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                    throw new FtpException("File upload failed with message: " + ioe.getMessage());
                }
            }
        }
    }
