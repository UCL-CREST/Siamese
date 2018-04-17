    public Boolean connect() throws Exception {
        try {
            _ftpClient = new FTPClient();
            _ftpClient.connect(_url);
            _ftpClient.login(_username, _password);
            _rootPath = _ftpClient.printWorkingDirectory();
            return true;
        } catch (Exception ex) {
            throw new Exception("Cannot connect to server.");
        }
    }
