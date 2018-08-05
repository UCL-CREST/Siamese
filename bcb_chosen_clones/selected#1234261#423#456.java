    private FTPClient getFtpClient(Entry parentEntry) throws Exception {
        Object[] values = parentEntry.getValues();
        if (values == null) {
            return null;
        }
        String server = (String) values[COL_SERVER];
        String baseDir = (String) values[COL_BASEDIR];
        String user = (String) values[COL_USER];
        String password = (String) values[COL_PASSWORD];
        if (password != null) {
            password = getRepository().getPageHandler().processTemplate(password, false);
        } else {
            password = "";
        }
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server);
            if (user != null) {
                ftpClient.login(user, password);
            }
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                return null;
            }
            ftpClient.setFileType(FTP.IMAGE_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            return ftpClient;
        } catch (Exception exc) {
            System.err.println("Could not connect to ftp server:" + exc);
            return null;
        }
    }
