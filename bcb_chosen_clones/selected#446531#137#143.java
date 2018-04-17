    private String createDefaultRepoConf() throws IOException {
        InputStream confIn = getClass().getResourceAsStream(REPO_CONF_PATH);
        File tempConfFile = File.createTempFile("repository", "xml");
        tempConfFile.deleteOnExit();
        IOUtils.copy(confIn, new FileOutputStream(tempConfFile));
        return tempConfFile.getAbsolutePath();
    }
