    private void initializeServer() throws IOException, FTPException {
        fClient = new FTPClient();
        fClient.setRemoteHost(rHost);
        fClient.connect();
        fClient.login(user, passwd);
    }
