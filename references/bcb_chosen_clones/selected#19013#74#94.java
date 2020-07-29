    private FTPClient connect() throws IOException {
        FTPClient client = null;
        Configuration conf = getConf();
        String host = conf.get("fs.ftp.host");
        int port = conf.getInt("fs.ftp.host.port", FTP.DEFAULT_PORT);
        String user = conf.get("fs.ftp.user." + host);
        String password = conf.get("fs.ftp.password." + host);
        client = new FTPClient();
        client.connect(host, port);
        int reply = client.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            throw new IOException("Server - " + host + " refused connection on port - " + port);
        } else if (client.login(user, password)) {
            client.setFileTransferMode(FTP.BLOCK_TRANSFER_MODE);
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.setBufferSize(DEFAULT_BUFFER_SIZE);
        } else {
            throw new IOException("Login failed on server - " + host + ", port - " + port);
        }
        return client;
    }
