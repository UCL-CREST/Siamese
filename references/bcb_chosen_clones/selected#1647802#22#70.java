    public boolean uploadFromServlet(InputStream is, String serverFileName, String serverPath, String serverUrl, int serverPort, String userName, String passWord) throws IOException {
        FTPClient ftp = new FTPClient();
        FTPClientConfig conf = new FTPClientConfig();
        conf.setServerLanguageCode("zh_CN");
        conf.setServerTimeZoneId("Asia/Chongqing");
        try {
            ftp.configure(conf);
            int reply;
            ftp.setDefaultPort(serverPort);
            ftp.connect(serverUrl);
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new IOException("FTP server refused connection.");
            }
        } catch (IOException e) {
            disconnectFtp(ftp);
        }
        try {
            if (!ftp.login(userName, passWord)) {
                throw new IOException("Can not log in with given username and password.");
            }
            if (!ftp.changeWorkingDirectory(serverPath)) {
                throw new IOException("Can not change to working directory.");
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            if (!ftp.storeFile(serverFileName, is)) {
                throw new IOException("Can not store file to FTP server.");
            }
            is.close();
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ftp != null && ftp.isConnected()) {
                try {
                    ftp.logout();
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }
