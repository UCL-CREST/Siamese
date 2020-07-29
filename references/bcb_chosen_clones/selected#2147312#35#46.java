    public void connect() throws SocketException, IOException {
        Log.i(TAG, "Test attempt login to " + ftpHostname + " as " + ftpUsername);
        ftpClient = new FTPClient();
        ftpClient.connect(this.ftpHostname, this.ftpPort);
        ftpClient.login(ftpUsername, ftpPassword);
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            String error = "Login failure (" + reply + ") : " + ftpClient.getReplyString();
            Log.e(TAG, error);
            throw new IOException(error);
        }
    }
