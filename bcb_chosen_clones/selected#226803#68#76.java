    public void connect() throws IOException {
        if (log.isDebugEnabled()) log.debug("Connecting to: " + HOST);
        ftpClient.connect(HOST);
        if (log.isDebugEnabled()) log.debug("\tReply: " + ftpClient.getReplyString());
        if (log.isDebugEnabled()) log.debug("Login as anonymous");
        ftpClient.login("anonymous", "");
        if (log.isDebugEnabled()) log.debug("\tReply: " + ftpClient.getReplyString());
        folder = INTACT_FOLDER;
    }
