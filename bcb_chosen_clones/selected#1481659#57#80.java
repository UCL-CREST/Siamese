    @Override
    protected void setUp() throws Exception {
        this.logger = new ConsoleLogger(ConsoleLogger.LEVEL_WARN);
        File repoFolder = new File("target/repository");
        removeRepository(repoFolder);
        InputStream repoConfigIn = getClass().getResourceAsStream(REPO_CONFIG_FILE);
        File tempRepoConfigFile = File.createTempFile("repository", "xml");
        tempRepoConfigFile.deleteOnExit();
        OutputStream tempRepoConfigOut = new FileOutputStream(tempRepoConfigFile);
        try {
            IOUtils.copy(repoConfigIn, tempRepoConfigOut);
        } finally {
            repoConfigIn.close();
            tempRepoConfigOut.close();
        }
        Repository repo = new TransientRepository(tempRepoConfigFile.getAbsolutePath(), "target/repository");
        ServerAdapterFactory factory = new ServerAdapterFactory();
        RemoteRepository remoteRepo = factory.getRemoteRepository(repo);
        reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        reg.rebind(REMOTE_REPO_NAME, remoteRepo);
        session = repo.login(new SimpleCredentials(LOGIN, PWD.toCharArray()), WORKSPACE);
        InputStream nodeTypeDefIn = getClass().getResourceAsStream(MQ_JCR_XML_NODETYPES_FILE);
        JackrabbitInitializerHelper.setupRepository(session, new InputStreamReader(nodeTypeDefIn), "");
    }
