    private void initialiseDatabaseRepository() throws Exception {
        DatabaseModel databaseModel = browserConfigurationModel.getDatabaseModel();
        String databaseClassName = databaseModel.getDatabaseClassName();
        if (databaseClassName.equals("pierre.db.DummyDataRepository") == true) {
            DummyDataRepository dummyDataRepository = new DummyDataRepository();
            BrowseModel browseModel = browserConfigurationModel.getBrowseModel();
            ArrayList browseAttributes = browseModel.getBrowseAttributes();
            dummyDataRepository.setBrowseAttributes(browseAttributes);
            dataRepository = (DataRepository) dummyDataRepository;
        } else {
            ClassLoader classLoader = getClass().getClassLoader();
            Class repositoryClass = null;
            if (enableClassLoader == true) {
                ClassLoaderUtility classLoaderUtility = new ClassLoaderUtility(classLoader);
                URL[] urls = databaseModel.getDependencies();
                classLoaderUtility.addURLs(databaseModel.getDependencies());
                repositoryClass = classLoaderUtility.findClass(databaseClassName);
            } else {
                repositoryClass = Class.forName(databaseClassName);
            }
            Constructor constructor = repositoryClass.getConstructor(new Class[0]);
            dataRepository = (DataRepository) constructor.newInstance(new Object[0]);
        }
        dataRepository.setParameters(databaseModel.getParameters());
    }
