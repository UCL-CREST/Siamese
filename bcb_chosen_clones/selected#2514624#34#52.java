    public void getDownloadInfo(String _url) throws Exception {
        cl = new FTPClient();
        Authentication auth = new FTPAuthentication();
        cl.connect(getHostName());
        while (!cl.login(auth.getUser(), auth.getPassword())) {
            log.debug("getDownloadInfo() - login error state: " + Arrays.asList(cl.getReplyStrings()));
            ap.setSite(getSite());
            auth = ap.promptAuthentication();
            if (auth == null) throw new Exception("User Cancelled Auth Operation");
        }
        AuthManager.putAuth(getSite(), auth);
        cl.enterLocalPassiveMode();
        FTPFile file = cl.listFiles(new URL(_url).getFile())[0];
        setURL(_url);
        setLastModified(file.getTimestamp().getTimeInMillis());
        setSize(file.getSize());
        setResumable(cl.rest("0") == 350);
        setRangeEnd(getSize() - 1);
    }
