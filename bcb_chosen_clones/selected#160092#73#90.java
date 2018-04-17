    private void transferFile(String fileName) throws SocketException, IOException, Exception {
        FTPClient client = new FTPClient();
        client.connect(server.getExternalName(), server.getPort());
        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new Exception("Failed connecting to server");
        }
        client.login(server.getDefaultUserName(), server.getDefaultUserPassword());
        reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new Exception("Failed connecting to server");
        }
        InputStream stream = getClass().getClassLoader().getResourceAsStream("res/conf/ftpd.properties");
        client.storeFile(fileName, stream);
        File transfferedFile = new File(server.getServerRootDirectory(), fileName);
        assertTrue(transfferedFile.exists());
        assertTrue(transfferedFile.delete());
    }
