    public void delete(String fileToDelete) throws IOException {
        FTPClient ftp = new FTPClient();
        try {
            int reply = 0;
            ftp.connect(this.endpointURL, Config.getFtpPort());
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new IOException("Ftp delete server refused connection.");
            }
            if (!ftp.login("anonymous", "")) {
                ftp.logout();
                throw new IOException("FTP: server wrong passwd");
            }
            ftp.enterLocalPassiveMode();
            log.debug("Deleted: " + ftp.deleteFile(fileToDelete));
            ftp.logout();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
