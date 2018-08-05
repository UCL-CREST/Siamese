    public final void conectar() throws IOException, FTPException {
        ftp = null;
        ftp = new FTPClient();
        ftp.setRemoteHost(cfg.getFTPHost());
        ftp.connect();
        ftp.login(cfg.getFTPUser(), cfg.getFTPPass());
        ftp.setProgressMonitor(pMonitor);
        ftp.setConnectMode(FTPConnectMode.PASV);
        ftp.setType(FTPTransferType.BINARY);
    }
