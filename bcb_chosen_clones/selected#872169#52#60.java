    private boolean connect() {
        try {
            this.ftpClient.connect(this.server, this.port);
            this.ftpClient.login(this.username, this.password);
            return true;
        } catch (IOException iOException) {
            return false;
        }
    }
