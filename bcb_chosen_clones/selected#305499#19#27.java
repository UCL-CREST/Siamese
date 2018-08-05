    public static FTPClient getClient(String serverAddress, String login, String password, boolean PASV) throws SocketException, IOException {
        FTPClient client = new FTPClient();
        client.connect(serverAddress);
        if (PASV) {
            client.enterLocalPassiveMode();
        }
        client.login(login, password);
        return client;
    }
