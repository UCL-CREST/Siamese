    private synchronized void createFTPConnection() throws FTPBrowseException {
        ftpClient = new FTPClient();
        try {
            InetAddress inetAddress = InetAddress.getByName(url.getHost());
            if (url.getPort() == -1) {
                ftpClient.connect(inetAddress);
            } else {
                ftpClient.connect(inetAddress, url.getPort());
            }
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                throw new FTPBrowseException(ftpClient.getReplyString());
            }
            if (null != passwordAuthentication) {
                ftpClient.login(passwordAuthentication.getUserName(), new StringBuffer().append(passwordAuthentication.getPassword()).toString());
            }
            if (url.getPath().length() > 0) {
                ftpClient.changeWorkingDirectory(url.getPath());
            }
            homeDirectory = ftpClient.printWorkingDirectory();
        } catch (UnknownHostException e) {
            throw new FTPBrowseException(e.getMessage());
        } catch (SocketException e) {
            throw new FTPBrowseException(e.getMessage());
        } catch (FTPBrowseException e) {
            throw e;
        } catch (IOException e) {
            throw new FTPBrowseException(e.getMessage());
        }
    }
