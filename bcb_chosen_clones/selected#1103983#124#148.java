    public boolean exists(String fileToCheck) throws IOException {
        FTPClient ftp = new FTPClient();
        boolean found = false;
        try {
            int reply = 0;
            ftp.connect(this.endpointURL, Config.getFtpPort());
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new IOException("Ftp exists server refused connection.");
            }
            if (!ftp.login("anonymous", "")) {
                ftp.logout();
                throw new IOException("FTP: server wrong passwd");
            }
            ftp.enterLocalPassiveMode();
            if (ftp.listNames(fileToCheck) != null) {
                found = true;
            }
            ftp.logout();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        return found;
    }
