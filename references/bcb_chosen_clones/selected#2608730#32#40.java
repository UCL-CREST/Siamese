    protected void connect() throws SocketException, IOException, LoginFailException {
        logger.info("Connect to FTP Server " + account.getServer());
        client = new FTPClient();
        client.connect(account.getServer());
        if (client.login(account.getId(), account.getPassword()) == false) {
            logger.info("Fail to login with id=" + account.getId());
            throw new LoginFailException(account.getId(), account.getPassword());
        }
    }
