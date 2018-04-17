    public void connect(String ftpHost, int ftpPort, String ftpUser, String ftpPwd) throws IOException {
        ftpClient = new FTPClient();
        ftpClient.setReaderThread(false);
        if (ftpPort == -1) ftpClient.connect(ftpHost); else ftpClient.connect(ftpHost, ftpPort);
        logger.info("FTP Connection Successful: " + ftpHost);
        ftpClient.login(ftpUser, ftpPwd);
    }
