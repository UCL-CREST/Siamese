    public boolean connect() {
        boolean isConnected = false;
        try {
            try {
                this.ftpClient.connect(this.server, this.port);
            } catch (SocketException e) {
                status = ErrorResult.CONNECTNOTPOSSIBLE.code;
                return false;
            } catch (IOException e) {
                status = ErrorResult.CONNECTNOTPOSSIBLE.code;
                return false;
            }
            int reply = this.ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.disconnect();
                status = ErrorResult.CONNECTNOTCORRECT.code;
                return false;
            }
            try {
                if (this.account == null) {
                    if (!this.ftpClient.login(this.username, this.passwd)) {
                        status = ErrorResult.LOGINNOTCORRECT.code;
                        this.ftpClient.logout();
                        return false;
                    }
                } else if (!this.ftpClient.login(this.username, this.passwd, this.account)) {
                    status = ErrorResult.LOGINACCTNOTCORRECT.code;
                    this.ftpClient.logout();
                    return false;
                }
            } catch (IOException e) {
                status = ErrorResult.ERRORWHILECONNECT.code;
                try {
                    this.ftpClient.logout();
                } catch (IOException e1) {
                }
                return false;
            }
            isConnected = true;
            return true;
        } finally {
            if ((!isConnected) && this.ftpClient.isConnected()) {
                this.disconnect();
            }
        }
    }
