    public void login(LoginData loginData) throws ConnectionEstablishException, AccessDeniedException {
        try {
            int reply;
            this.ftpClient.connect(loginData.getFtpServer());
            reply = this.ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.ftpClient.disconnect();
                throw (new ConnectionEstablishException("FTP server refused connection."));
            }
        } catch (IOException e) {
            if (this.ftpClient.isConnected()) {
                try {
                    this.ftpClient.disconnect();
                } catch (IOException f) {
                }
            }
            e.printStackTrace();
            throw (new ConnectionEstablishException("Could not connect to server.", e));
        }
        try {
            if (!this.ftpClient.login(loginData.getFtpBenutzer(), loginData.getFtpPasswort())) {
                this.logout();
                throw (new AccessDeniedException("Could not login into server."));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw (new AccessDeniedException("Could not login into server.", ioe));
        }
    }
