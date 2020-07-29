    public void put(String path, File fileToPut) throws IOException {
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(this.endpointURL, this.endpointPort);
            log.debug("Ftp put reply: " + ftp.getReplyString());
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new IOException("Ftp put server refused connection.");
            }
            if (!ftp.login("anonymous", "")) {
                ftp.logout();
                throw new IOException("FTP: server wrong passwd");
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            InputStream input = new FileInputStream(fileToPut);
            if (ftp.storeFile(path, input) != true) {
                ftp.logout();
                input.close();
                throw new IOException("FTP put exception");
            }
            input.close();
            ftp.logout();
        } catch (Exception e) {
            log.error("Ftp client exception: " + e.getMessage(), e);
            throw new IOException(e.getMessage());
        }
    }
