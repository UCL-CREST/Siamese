    public boolean connect() {
        try {
            int reply;
            ftp.connect(server, port);
            reply = ftp.getReplyCode();
            if (FTPReply.isPositiveCompletion(reply)) {
                if (ftp.login(username, password)) {
                    ftp.enterLocalPassiveMode();
                    return true;
                }
            } else {
                ftp.disconnect();
                System.out.println("FTP server refused connection.");
            }
        } catch (IOException e) {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                }
            }
            System.out.println("Could not connect to server.");
        }
        return false;
    }
