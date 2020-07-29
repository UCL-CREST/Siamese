    protected FTPClient openFTP() throws CruiseControlException {
        LOG.info("Opening FTP connection to " + targetHost);
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(targetHost, targetPort);
            if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                throw new CruiseControlException("FTP connection failed: " + ftp.getReplyString());
            }
            LOG.info("logging in to FTP server");
            if (!ftp.login(targetUser, targetPasswd)) {
                throw new CruiseControlException("Could not login to FTP server");
            }
            LOG.info("login succeeded");
            if (passive) {
                setPassive(ftp);
            }
        } catch (IOException ioe) {
            LOG.error(ioe);
            throw new CruiseControlException(ioe.getMessage());
        }
        return ftp;
    }
