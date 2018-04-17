    private byte[] getFileFromFtp(String remote) throws Exception {
        ftp = new FTPClient();
        int reply;
        ftp.connect(ftpServer);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("FTP server refused connection.");
        }
        if (!ftp.login(ftpUsername, ftpPassword)) {
            ftp.logout();
            throw new Exception("Cann't login to ftp.");
        }
        ftp.enterLocalPassiveMode();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ftp.retrieveFile(remote, baos);
        ftp.logout();
        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException f) {
            }
        }
        return baos.toByteArray();
    }
