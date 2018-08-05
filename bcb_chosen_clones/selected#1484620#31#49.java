    String connect() throws IOException {
        String reply = null;
        if (ftp == null) {
            FTPClient ftp = new FTPClient();
            ftp.connect(remote);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                throw new IOException("Connection failed: " + remote);
            }
            reply = ftp.getReplyString();
            if (!ftp.login("anonymous", "")) {
                throw new IOException("Login failed: " + remote);
            }
            if (!ftp.setFileType(FTP.BINARY_FILE_TYPE)) {
                throw new IOException("Setting binary file type failed: " + remote);
            }
            this.ftp = ftp;
        }
        return reply;
    }
