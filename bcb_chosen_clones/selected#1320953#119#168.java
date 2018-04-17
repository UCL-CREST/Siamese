    public FTPFile[] connect() {
        if (ftpe == null) {
            ftpe = new FTPEvent(this);
        }
        if (ftp == null) {
            ftp = new FTPClient();
        } else if (ftp.isConnected()) {
            path = "";
            try {
                ftp.disconnect();
            } catch (IOException e1) {
                log.error("could not disconnect -" + e1.getMessage());
            }
        }
        currentDir = new FTPFile[0];
        log.debug("try to connect");
        try {
            int reply;
            ftp.connect(ftpsite);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                log.error("FTP server refused connection.");
            }
        } catch (IOException e) {
            log.error("FTPConnection error: " + e.getMessage());
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                }
            }
        }
        try {
            if (!ftp.login(user, password)) {
                log.error("could not login with: " + user);
                ftp.logout();
            }
            log.debug("Remote system is " + ftp.getSystemName());
            ftp.enterLocalPassiveMode();
            currentDir = ftp.listFiles();
        } catch (FTPConnectionClosedException e) {
            log.error("FTPConnectionClosedException: " + e.getMessage());
        } catch (IOException e) {
            log.error("IOException: " + e.getMessage());
        }
        ftpe.setType(FTPEvent.CONNECT);
        fireFTPEvent(ftpe);
        return currentDir;
    }
