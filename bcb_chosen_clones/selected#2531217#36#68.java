    public void ftpUpload() {
        FTPClient ftpclient = null;
        InputStream is = null;
        try {
            ftpclient = new FTPClient();
            ftpclient.connect(host, port);
            if (logger.isDebugEnabled()) {
                logger.debug("FTP连接远程服务器：" + host);
            }
            ftpclient.login(user, password);
            if (logger.isDebugEnabled()) {
                logger.debug("登陆用户：" + user);
            }
            ftpclient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpclient.changeWorkingDirectory(remotePath);
            is = new FileInputStream(localPath + File.separator + filename);
            ftpclient.storeFile(filename, is);
            logger.info("上传文件结束...路径：" + remotePath + "，文件名：" + filename);
            is.close();
            ftpclient.logout();
        } catch (IOException e) {
            logger.error("上传文件失败", e);
        } finally {
            if (ftpclient.isConnected()) {
                try {
                    ftpclient.disconnect();
                } catch (IOException e) {
                    logger.error("断开FTP出错", e);
                }
            }
            ftpclient = null;
        }
    }
