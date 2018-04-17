    @Override
    public void start() {
        try {
            ftp = new FTPClient();
            ftp.connect(this.url.getHost(), this.url.getPort() == -1 ? this.url.getDefaultPort() : this.url.getPort());
            String username = "anonymous";
            String password = "";
            if (this.url.getUserInfo() != null) {
                username = this.url.getUserInfo().split(":")[0];
                password = this.url.getUserInfo().split(":")[1];
            }
            ftp.login(username, password);
            long startPos = 0;
            if (getFile().exists()) startPos = getFile().length(); else getFile().createNewFile();
            ftp.download(this.url.getPath(), getFile(), startPos, new FTPDTImpl());
            ftp.disconnect(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            speedTimer.cancel();
        }
    }
