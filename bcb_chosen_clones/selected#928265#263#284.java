    private FTPClient connectFtps() throws NoSuchAlgorithmException, IOException {
        FTPClient apacheClient;
        if (isSecure) {
            apacheClient = new FTPSClient(true);
        } else {
            apacheClient = new FTPClient();
        }
        apacheClient.addProtocolCommandListener(new LogFtpListener(LOG));
        if (isSecure) {
            apacheClient.connect(host, 990);
        } else {
            apacheClient.connect(host);
        }
        if (!apacheClient.login(user, pass)) {
            throw new IllegalArgumentException("Unrecognized Username/Password");
        }
        apacheClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        apacheClient.getStatus();
        apacheClient.help();
        apacheClient.enterLocalPassiveMode();
        return apacheClient;
    }
