    public void openFtpConnection(String workingDirectory) throws RQLException {
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(server);
            ftpClient.login(user, password);
            ftpClient.changeWorkingDirectory(workingDirectory);
        } catch (IOException ioex) {
            throw new RQLException("FTP client could not be created. Please check attributes given in constructor.", ioex);
        }
    }
