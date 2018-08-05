    public FTPSource(SourceDetail sourceDetail) throws SourceException {
        this.sourceDetail = sourceDetail;
        localPathMap = new HashMap<String, String>();
        client = new FTPClient();
        try {
            client.connect(sourceDetail.getHost());
            client.login(sourceDetail.getUser(), sourceDetail.getPassword());
            workingDirectory = new File(ConfigManager.getGUIConfig().getWorkingDirectoryName() + File.separator + sourceDetail.getName());
            workingDirectory.mkdir();
        } catch (Exception e) {
            throw new SourceException(e);
        }
    }
