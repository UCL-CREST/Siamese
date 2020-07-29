    public void transport(File file) throws TransportException {
        FTPClient client = new FTPClient();
        try {
            client.connect(getOption("host"));
            client.login(getOption("username"), getOption("password"));
            client.changeWorkingDirectory(getOption("remotePath"));
            transportRecursive(client, file);
            client.disconnect();
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }
