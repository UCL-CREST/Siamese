    public void connect() throws FTPException {
        try {
            ftp = new FTPClient();
            ftp.connect(host);
            if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.login(this.username, this.password);
            } else {
                ftp.disconnect();
                throw new FTPException("Não foi possivel se conectar no servidor FTP");
            }
            isConnected = true;
        } catch (Exception ex) {
            throw new FTPException(ex);
        }
    }
