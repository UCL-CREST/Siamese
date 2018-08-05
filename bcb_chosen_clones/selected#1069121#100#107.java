    @Override
    protected void setUp() throws Exception {
        super.setUp();
        client = new FTPClient();
        client.connect(hostName);
        client.login("anonymous", "anonymous");
        client.enterLocalPassiveMode();
    }
