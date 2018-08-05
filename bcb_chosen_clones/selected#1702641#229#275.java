    private FTPClient getFTPConnection(String strUser, String strPassword, String strServer, boolean binaryTransfer, String connectionNote) {
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(strServer);
            ResourcePool.LogMessage(this, ResourcePool.INFO_MESSAGE, "Connected to " + strServer + ", " + connectionNote);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                ResourcePool.LogMessage(this, ResourcePool.ERROR_MESSAGE, "FTP server refused connection.");
                return null;
            }
        } catch (IOException e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    return null;
                }
            }
            ResourcePool.LogMessage(this, ResourcePool.ERROR_MESSAGE, "FTP Could not connect to server.");
            ResourcePool.LogException(e, this);
            return null;
        }
        try {
            if (!ftp.login(strUser, strPassword)) {
                ftp.logout();
                ResourcePool.LogMessage(this, ResourcePool.ERROR_MESSAGE, "FTP login failed.");
                return null;
            }
            ResourcePool.LogMessage(this, ResourcePool.INFO_MESSAGE, "Remote system is " + ftp.getSystemName() + ", " + connectionNote);
            if (binaryTransfer) {
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
            } else {
                ftp.setFileType(FTP.ASCII_FILE_TYPE);
            }
            ftp.enterLocalPassiveMode();
        } catch (FTPConnectionClosedException e) {
            ResourcePool.LogMessage(this, ResourcePool.ERROR_MESSAGE, "Server closed connection.");
            ResourcePool.LogException(e, this);
            return null;
        } catch (IOException e) {
            ResourcePool.LogException(e, this);
            return null;
        }
        return ftp;
    }
