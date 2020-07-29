    public FTPClient connect(String host, String userId, String password, String alias) throws IOException {
        FTPClient client = null;
        if (connections.get(alias) != null) {
            client = (FTPClient) connections.get(alias);
            if (client.isConnected() == false) {
                client.connect(host);
            }
        } else {
            client = new FTPClient();
            client.connect(host);
            client.login(userId, password);
            connections.put(alias, client);
        }
        return client;
    }
