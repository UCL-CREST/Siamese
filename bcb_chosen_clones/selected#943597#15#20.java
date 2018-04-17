    public void synchronize(String ftpServer, String user, String password) throws SocketException, IOException {
        client = new FTPClient();
        client.connect(ftpServer);
        client.login(user, password);
        this.loadContents("/");
    }
