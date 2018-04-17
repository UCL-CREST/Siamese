    public FTPClient getFTP(final Credentials credentials, final String remoteFile) throws NumberFormatException, SocketException, IOException, AccessDeniedException {
        String fileName = extractFilename(remoteFile);
        String fileDirectory = getPathName(remoteFile).substring(0, getPathName(remoteFile).indexOf(fileName));
        FTPClient ftp;
        ftp = new FTPClient();
        loadConfig();
        logger.info("FTP connection to: " + extractHostname(remoteFile));
        logger.info("FTP PORT: " + prop.getProperty("port"));
        ftp.connect(extractHostname(remoteFile), Integer.parseInt(prop.getProperty("port")));
        int reply = ftp.getReplyCode();
        if (!(FTPReply.isPositiveCompletion(reply))) {
            return null;
        }
        ftp.setFileTransferMode(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        ftp.setFileType(org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE);
        if (!ftp.login(credentials.getUserName(), credentials.getPassword())) {
            throw new AccessDeniedException(prop.getProperty("login_message"));
        }
        if (fileDirectory != null) {
            ftp.changeWorkingDirectory(fileDirectory);
        }
        return ftp;
    }
