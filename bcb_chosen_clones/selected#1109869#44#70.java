    public void play() throws FileNotFoundException, IOException, NoSuchAlgorithmException, FTPException {
        final int BUFFER = 2048;
        String host = "ftp.genome.jp";
        String username = "anonymous";
        String password = "";
        FTPClient ftp = null;
        ftp = new FTPClient();
        ftp.setRemoteHost(host);
        FTPMessageCollector listener = new FTPMessageCollector();
        ftp.setMessageListener(listener);
        System.out.println("Connecting");
        ftp.connect();
        System.out.println("Logging in");
        ftp.login(username, password);
        System.out.println("Setting up passive, ASCII transfers");
        ftp.setConnectMode(FTPConnectMode.PASV);
        ftp.setType(FTPTransferType.ASCII);
        System.out.println("Directory before put:");
        String[] files = ftp.dir(".", true);
        for (int i = 0; i < files.length; i++) System.out.println(files[i]);
        System.out.println("Quitting client");
        ftp.quit();
        String messages = listener.getLog();
        System.out.println("Listener log:");
        System.out.println(messages);
        System.out.println("Test complete");
    }
