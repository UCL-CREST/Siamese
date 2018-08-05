    public boolean login() {
        if (super.isAuthenticated()) return true;
        try {
            if (client == null) {
                client = new FTPClient();
                FTPClientConfig config = new FTPClientConfig();
                client.configure(config);
            }
            if (!client.isConnected()) {
                client.connect(super.getStoreConfig().getServerName(), new Integer(super.getStoreConfig().getServerPort()).intValue());
            }
            if (client.login(super.getStoreConfig().getUserName(), super.getStoreConfig().getPassword(), super.getStoreConfig().getServerName())) {
                super.setAuthenticated(true);
                return true;
            }
            log.error("Login ftp server error");
        } catch (Exception e) {
            log.info("FTPStore.login", e);
        }
        return false;
    }
