    private void establish() throws IOException {
        ftpClient = new FTPClient();
        ftpClient.connect("localhost", 8021);
        ftpClient.login("anonymous", "test@test.com");
    }
