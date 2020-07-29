    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/optional/net/ftp.xml");
        getProject().executeTarget("setup");
        tmpDir = getProject().getProperty("tmp.dir");
        ftp = new FTPClient();
        ftpFileSep = getProject().getProperty("ftp.filesep");
        myFTPTask.setSeparator(ftpFileSep);
        myFTPTask.setProject(getProject());
        remoteTmpDir = myFTPTask.resolveFile(tmpDir);
        String remoteHost = getProject().getProperty("ftp.host");
        int port = Integer.parseInt(getProject().getProperty("ftp.port"));
        String remoteUser = getProject().getProperty("ftp.user");
        String password = getProject().getProperty("ftp.password");
        try {
            ftp.connect(remoteHost, port);
        } catch (Exception ex) {
            connectionSucceeded = false;
            loginSuceeded = false;
            System.out.println("could not connect to host " + remoteHost + " on port " + port);
        }
        if (connectionSucceeded) {
            try {
                ftp.login(remoteUser, password);
            } catch (IOException ioe) {
                loginSuceeded = false;
                System.out.println("could not log on to " + remoteHost + " as user " + remoteUser);
            }
        }
    }
