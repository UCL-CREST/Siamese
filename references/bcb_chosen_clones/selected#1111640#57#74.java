    private FTPClient connect() throws FTPException {
        try {
            FTPClient ftp = new FTPClient();
            ftp.connect(host, port);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
            }
            ftp.login(userName, password);
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            return ftp;
        } catch (SocketException e) {
            throw new FTPException("Failed to connect to server", e);
        } catch (IOException e) {
            throw new FTPException("Failed to connect to server", e);
        }
    }
