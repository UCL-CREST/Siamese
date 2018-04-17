    public UploadHubList(String server, String username, String password, String remoteFile, String filePath) throws SocketException, IOException {
        FTPClient ftp = new FTPClient();
        System.out.println("\t.");
        ftp.connect(server);
        System.out.println("\t..");
        ftp.login(username, password);
        System.out.print(ftp.getReplyString());
        System.out.println("\t...");
        ftp.storeFile(remoteFile, new FileInputStream(filePath));
        System.out.print(ftp.getReplyString());
    }
