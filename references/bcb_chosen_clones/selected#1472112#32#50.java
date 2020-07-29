    public FTPUtil(final String server) {
        log.debug("~ftp.FTPUtil() : Creating object");
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(server);
            ftpClient.login("anonymous", "");
            ftpClient.setConnectTimeout(120000);
            ftpClient.setSoTimeout(120000);
            final int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                final String errMsg = "Non-positive completion connecting FTPClient";
                log.warn("~ftp.FTPUtil() : [" + errMsg + "]");
            }
        } catch (IOException ioe) {
            final String errMsg = "Cannot connect and login to ftpClient [" + ioe.getMessage() + "]";
            log.warn("~ftp.FTPUtil() : [" + errMsg + "]");
            ioe.printStackTrace();
        }
    }
