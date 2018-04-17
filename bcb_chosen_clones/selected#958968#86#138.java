    public void putFullDirectory(final String ftpURL, final String remoteDir, final String userId, final String pwd, final String localDir) throws Exception {
        if (!Strings.isPopulated(ftpURL)) {
            Util.dspmsg("Need an FTP url.");
            return;
        }
        if (!Strings.isPopulated(remoteDir)) {
            Util.dspmsg("Need a remote directory.");
            return;
        }
        if (!Strings.isPopulated(userId)) {
            Util.dspmsg("Need a user ID.");
            return;
        }
        if (!Strings.isPopulated(pwd)) {
            Util.dspmsg("Need a password.");
            return;
        }
        if (!Strings.isPopulated(localDir)) {
            Util.dspmsg("Need a local directory.");
            return;
        }
        FTPClient c = new FTPClient();
        c.connect(ftpURL);
        int replyCode = c.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            Util.dspmsg("Could not connect, code: " + replyCode);
            c.disconnect();
            return;
        }
        if (!c.login(userId, pwd)) {
            Util.dspmsg("Could not log on, userId: " + userId + " pwd: " + pwd);
            return;
        }
        StringTokenizer st = new StringTokenizer(remoteDir, "/");
        while (st.hasMoreElements()) {
            if (!chgDir(c, st.nextToken())) {
                return;
            }
        }
        c.setFileType(FTP.BINARY_FILE_TYPE);
        File file = new File(localDir);
        if (file.isDirectory()) {
            FOR: for (File f : file.listFiles()) {
                if (!put(c, f)) {
                    break FOR;
                }
            }
        } else {
            put(c, file);
        }
        c.logout();
        c.disconnect();
    }
