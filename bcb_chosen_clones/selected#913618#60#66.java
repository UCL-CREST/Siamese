    public boolean testConnection(FTPSite site) throws IOException {
        FTPClient testclient = new FTPClient();
        testclient.connect(site.getHost(), site.getPort());
        boolean check = testclient.login(site.getUser(), site.getPassword());
        testclient.disconnect();
        return check;
    }
