    public boolean connentServer() {
        boolean result = false;
        try {
            ftpClient = new FTPClient();
            ftpClient.setDefaultPort(port);
            ftpClient.setControlEncoding("GBK");
            strOut = strOut + "Connecting to host " + host + "\r\n";
            ftpClient.connect(host);
            if (!ftpClient.login(user, password)) return false;
            FTPClientConfig conf = new FTPClientConfig(getSystemKey(ftpClient.getSystemName()));
            conf.setServerLanguageCode("zh");
            ftpClient.configure(conf);
            strOut = strOut + "User " + user + " login OK.\r\n";
            if (!ftpClient.changeWorkingDirectory(sDir)) {
                ftpClient.makeDirectory(sDir);
                ftpClient.changeWorkingDirectory(sDir);
            }
            strOut = strOut + "Directory: " + sDir + "\r\n";
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            strOut = strOut + "Connect Success.\r\n";
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
