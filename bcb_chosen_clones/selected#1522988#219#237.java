    private void Connect() throws NpsException {
        try {
            client = new FTPClient();
            client.connect(host.hostname, host.remoteport);
            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                client = null;
                nps.util.DefaultLog.error_noexception("FTP Server:" + host.hostname + "refused connection.");
                return;
            }
            client.login(host.uname, host.upasswd);
            client.enterLocalPassiveMode();
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.changeWorkingDirectory(host.remotedir);
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        }
    }
