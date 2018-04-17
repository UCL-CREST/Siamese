    public Ftp(Resource resource, String basePath) throws Exception {
        super(resource, basePath);
        client = new FTPClient();
        client.addProtocolCommandListener(new CommandLogger());
        client.connect(resource.getString("host"), Integer.parseInt(resource.getString("port")));
        client.login(resource.getString("user"), resource.getString("pw"));
        client.setFileType(FTPClient.BINARY_FILE_TYPE);
        client.enterLocalPassiveMode();
    }
