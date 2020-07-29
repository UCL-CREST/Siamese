    public static void ftpUpload(FTPConfig config, String directory, File file, String remoteFileName) throws IOException {
        FTPClient server = new FTPClient();
        server.connect(config.host, config.port);
        assertValidReplyCode(server.getReplyCode(), server);
        server.login(config.userName, config.password);
        assertValidReplyCode(server.getReplyCode(), server);
        assertValidReplyCode(server.cwd(directory), server);
        server.setFileTransferMode(FTP.IMAGE_FILE_TYPE);
        server.setFileType(FTP.IMAGE_FILE_TYPE);
        server.storeFile(remoteFileName, new FileInputStream(file));
        assertValidReplyCode(server.getReplyCode(), server);
        server.sendNoOp();
        server.disconnect();
    }
