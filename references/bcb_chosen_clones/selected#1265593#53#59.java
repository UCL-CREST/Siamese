    protected FTPClient ftpConnect() throws SocketException, IOException, NoSuchAlgorithmException {
        FilePathItem fpi = getFilePathItem();
        FTPClient f = new FTPClient();
        f.connect(fpi.getHost());
        f.login(fpi.getUsername(), fpi.getPassword());
        return f;
    }
