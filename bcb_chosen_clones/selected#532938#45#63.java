    public void deploy(String baseDir, boolean clean) throws IOException {
        try {
            ftp.connect(hostname, port);
            log.debug("Connected to: " + hostname + ":" + port);
            ftp.login(username, password);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                throw new IOException("Error logging onto ftp server. FTPClient returned code: " + reply);
            }
            log.debug("Logged in");
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            if (clean) {
                deleteDir(remoteDir);
            }
            storeFolder(baseDir, remoteDir);
        } finally {
            ftp.disconnect();
        }
    }
