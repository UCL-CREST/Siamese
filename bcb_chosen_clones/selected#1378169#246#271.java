        protected final void connectFtp() throws IOException {
            try {
                if (!this.ftpClient.isConnected()) {
                    this.ftpClient.connect(getHost(), getPort());
                    getLog().write(Level.INFO, String.format(getMessages().getString("FtpSuccessfullyConnected"), getHost()));
                    int reply = this.ftpClient.getReplyCode();
                    if (!FTPReply.isPositiveCompletion(reply)) {
                        this.ftpClient.disconnect();
                        throw new IOException(String.format(getMessages().getString("FtpErrorConnectingRefused"), getHost()));
                    }
                    if (getUsername() != null) {
                        if (!this.ftpClient.login(getUsername(), getPassword())) {
                            this.ftpClient.logout();
                            disconnectFtp();
                            throw new IOException(String.format(getMessages().getString("FtpErrorAuthorizing"), getHost()));
                        }
                    }
                    this.ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    this.ftpClient.enterLocalPassiveMode();
                    getLog().write(Level.INFO, String.format(getMessages().getString("FtpSuccessfullyAuthorized"), getHost()));
                }
            } catch (IOException ex) {
                disconnectFtp();
                throw new IOException(String.format(getMessages().getString("FtpErrorConnecting"), getHost(), ex.toString()));
            }
        }
