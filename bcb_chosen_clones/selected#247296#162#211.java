    public boolean connectServer(String server, String user, String password) {
        boolean result = true;
        try {
            if (user.equals("")) {
                user = "anonymous";
                password = "anonymous";
            }
            this.server = server;
            this.user = user;
            this.password = password;
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding(encode);
            ftpClient.connect(server);
            ftpClient.setSoTimeout(1000 * 30);
            ftpClient.setDefaultTimeout(1000 * 30);
            ftpClient.setConnectTimeout(1000 * 30);
            ftpClient.enterLocalPassiveMode();
            ftpClient.login(user, password);
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                ftpClient.disconnect();
                return false;
            }
            queFilePath = "data\\" + this.server + ".que";
            bufFilePath = "data\\" + this.server + ".buf";
            startGetList();
        } catch (java.net.SocketTimeoutException e1) {
            errMsg = ftpClient.getReplyString();
            errCode = ftpClient.getReplyCode();
            result = false;
            setArrToFile(dirQueue, queFilePath);
            setArrToFile(fileList, bufFilePath);
            cn.imgdpu.util.CatException.getMethod().catException(e1, "连接超时");
        } catch (Exception e) {
            errMsg = ftpClient.getReplyString();
            errCode = ftpClient.getReplyCode();
            result = false;
            setArrToFile(dirQueue, queFilePath);
            setArrToFile(fileList, bufFilePath);
            cn.imgdpu.util.CatException.getMethod().catException(e, "未知异常");
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ioe) {
                    cn.imgdpu.util.CatException.getMethod().catException(ioe, "IO异常");
                }
            }
        }
        return result;
    }
