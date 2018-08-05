    public static void connectServer() {
        if (ftpClient == null) {
            int reply;
            try {
                setArg(configFile);
                ftpClient = new FTPClient();
                ftpClient.setDefaultPort(port);
                ftpClient.configure(getFtpConfig());
                ftpClient.connect(ip);
                ftpClient.login(username, password);
                ftpClient.setDefaultPort(port);
                System.out.print(ftpClient.getReplyString());
                reply = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftpClient.disconnect();
                    System.err.println("FTP server refused connection.");
                }
            } catch (Exception e) {
                System.err.println("��¼ftp��������" + ip + "��ʧ��");
                e.printStackTrace();
            }
        }
    }
