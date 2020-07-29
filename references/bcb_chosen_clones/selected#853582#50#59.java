    public void open(String server, String user, String pass, int port, String option) throws Exception {
        log.info("Login to FTP: " + server);
        this.port = port;
        ftp = new FTPClient();
        ftp.connect(server, port);
        ftp.login(user, pass);
        checkReply("FTP server refused connection." + server);
        modeBINARY();
        this.me = this;
    }
