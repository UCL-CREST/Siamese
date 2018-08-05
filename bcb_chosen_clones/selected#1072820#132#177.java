    private Dataset(File f, Properties p, boolean ro) throws DatabaseException {
        folder = f;
        logger.debug("Opening dataset [" + ((ro) ? "readOnly" : "read/write") + " mode]");
        readOnly = ro;
        logger = Logger.getLogger(Dataset.class);
        logger.debug("Opening environment: " + f);
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setTransactional(false);
        envConfig.setAllowCreate(!readOnly);
        envConfig.setReadOnly(readOnly);
        env = new Environment(f, envConfig);
        File props = new File(folder, "dataset.properties");
        if (!ro && p != null) {
            this.properties = p;
            try {
                FileOutputStream fos = new FileOutputStream(props);
                p.store(fos, null);
                fos.close();
            } catch (IOException e) {
                logger.warn("Error saving dataset properties", e);
            }
        } else {
            if (props.exists()) {
                try {
                    Properties pr = new Properties();
                    FileInputStream fis = new FileInputStream(props);
                    pr.load(fis);
                    fis.close();
                    this.properties = pr;
                } catch (IOException e) {
                    logger.warn("Error reading dataset properties", e);
                }
            }
        }
        getPaths();
        getNamespaces();
        getTree();
        pathDatabases = new HashMap();
        frequencyDatabases = new HashMap();
        lengthDatabases = new HashMap();
        clustersDatabases = new HashMap();
        pathMaps = new HashMap();
        frequencyMaps = new HashMap();
        lengthMaps = new HashMap();
        clustersMaps = new HashMap();
    }
