    public void beforeMethod(TestBase testBase) throws IOException {
        TFileFactory fileFactory = new TFileFactory(new InMemoryFileSystem());
        ftpServer.cleanFileSystem(fileFactory);
        TDirectory rootDir = fileFactory.dir("/");
        testBase.inject(rootDir);
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("localhost", 8021);
        ftpClient.login("anonymous", "test@test.com");
        testBase.inject(ftpClient);
    }
