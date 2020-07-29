    public static FTPClient getFtpClient(TopAnalysisConfig topAnalyzerConfig) throws SocketException, IOException {
        FTPClient ftp = new FTPClient();
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        ftp.connect(topAnalyzerConfig.getFtpServer());
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new java.lang.RuntimeException("PullFileJobWorker connect ftp error!");
        }
        if (!ftp.login(topAnalyzerConfig.getFtpUserName(), topAnalyzerConfig.getFtpPassWord())) {
            ftp.logout();
            throw new java.lang.RuntimeException("PullFileJobWorker login ftp error!");
        }
        logger.info("Remote system is " + ftp.getSystemName());
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        if (topAnalyzerConfig.isLocalPassiveMode()) ftp.enterLocalPassiveMode();
        return ftp;
    }
