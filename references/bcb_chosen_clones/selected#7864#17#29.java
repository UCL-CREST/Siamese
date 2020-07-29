    public FTPManager(URL location) throws IOException {
        this.client = new FTPClient();
        String host = location.getHost();
        int port = location.getPort();
        if (port < 0) {
            this.client.connect(host);
        } else {
            this.client.connect(host, port);
        }
        String[] login = StringUtils.split(location.getUserInfo(), ':');
        this.client.login(login[0], login.length > 1 ? login[1] : "");
        if (!this.client.setFileType(FTP.BINARY_FILE_TYPE)) throw new IOException("Unable to set the transfert mode");
    }
