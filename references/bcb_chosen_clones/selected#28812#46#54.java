    private void initFtp() throws IOException {
        ftpClient.setConnectTimeout(5000);
        ftpClient.connect(ftpHost);
        ftpClient.login(userName, password);
        if (workingDir != null) {
            ftpClient.changeWorkingDirectory(workingDir);
        }
        logger.info("Connection established.");
    }
