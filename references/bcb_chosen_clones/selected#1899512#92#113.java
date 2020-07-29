    private FTPClient getClient() throws SocketException, IOException {
        FTPClient ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftp.setDefaultPort(getPort());
        ftp.connect(getIp());
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            log.warn("FTP server refused connection: {}", getIp());
            ftp.disconnect();
            return null;
        }
        if (!ftp.login(getUsername(), getPassword())) {
            log.warn("FTP server refused login: {}, user: {}", getIp(), getUsername());
            ftp.logout();
            ftp.disconnect();
            return null;
        }
        ftp.setControlEncoding(getEncoding());
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
        return ftp;
    }
