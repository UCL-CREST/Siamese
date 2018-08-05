    private void _connect() throws SocketException, IOException {
        try {
            ftpClient.disconnect();
        } catch (Exception ex) {
        }
        ftpClient.connect(host, port);
        ftpClient.login("anonymous", "");
        ftpClient.enterLocalActiveMode();
    }
