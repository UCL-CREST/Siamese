    public List<String> getFtpFileList(String serverIp, int port, String user, String password, String synchrnPath) throws Exception {
        List<String> list = new ArrayList<String>();
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("euc-kr");
        if (!EgovWebUtil.isIPAddress(serverIp)) {
            throw new RuntimeException("IP is needed. (" + serverIp + ")");
        }
        InetAddress host = InetAddress.getByName(serverIp);
        ftpClient.connect(host, port);
        ftpClient.login(user, password);
        ftpClient.changeWorkingDirectory(synchrnPath);
        FTPFile[] fTPFile = ftpClient.listFiles(synchrnPath);
        for (int i = 0; i < fTPFile.length; i++) {
            list.add(fTPFile[i].getName());
        }
        return list;
    }
