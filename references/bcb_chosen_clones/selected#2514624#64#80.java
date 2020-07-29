    public void initGet() throws Exception {
        cl = new FTPClient();
        cl.connect(getHostName());
        Authentication auth = AuthManager.getAuth(getSite());
        if (auth == null) auth = new FTPAuthentication(getSite());
        while (!cl.login(auth.getUser(), auth.getPassword())) {
            ap.setSite(getSite());
            auth = ap.promptAuthentication();
            if (auth == null) throw new Exception("User Cancelled Auth Operation");
        }
        cl.connect(getHostName());
        cl.login(auth.getUser(), auth.getPassword());
        cl.enterLocalPassiveMode();
        cl.setFileType(FTP.BINARY_FILE_TYPE);
        cl.setRestartOffset(getPosition());
        setInputStream(cl.retrieveFileStream(new URL(getURL()).getFile()));
    }
