    public static void upload(FTPDetails ftpDetails) {
        FTPClient ftp = new FTPClient();
        try {
            String host = ftpDetails.getHost();
            logger.info("Connecting to ftp host: " + host);
            ftp.connect(host);
            logger.info("Received reply from ftp :" + ftp.getReplyString());
            ftp.login(ftpDetails.getUserName(), ftpDetails.getPassword());
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.makeDirectory(ftpDetails.getRemoterDirectory());
            logger.info("Created directory :" + ftpDetails.getRemoterDirectory());
            ftp.changeWorkingDirectory(ftpDetails.getRemoterDirectory());
            BufferedInputStream ftpInput = new BufferedInputStream(new FileInputStream(new File(ftpDetails.getLocalFilePath())));
            OutputStream storeFileStream = ftp.storeFileStream(ftpDetails.getRemoteFileName());
            IOUtils.copy(ftpInput, storeFileStream);
            logger.info("Copied file : " + ftpDetails.getLocalFilePath() + " >>> " + host + ":/" + ftpDetails.getRemoterDirectory() + "/" + ftpDetails.getRemoteFileName());
            ftpInput.close();
            storeFileStream.close();
            ftp.logout();
            ftp.disconnect();
            logger.info("Logged out. ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
