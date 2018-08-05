    private FTPClient loginToSharedWorkspace() throws SocketException, IOException {
        FTPClient ftp = new FTPClient();
        ftp.connect(mSwarm.getHost(), mSharedWorkspacePort);
        if (!ftp.login(SHARED_WORKSPACE_LOGIN_NAME, mWorkspacePassword)) {
            throw new IOException("Unable to login to shared workspace.");
        }
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        return ftp;
    }
