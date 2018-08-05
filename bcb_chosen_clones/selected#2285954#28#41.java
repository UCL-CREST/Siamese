    @Override
    public void run() {
        try {
            logger.info("Trying to create DummyDataSource...");
            Class dummyDataSourceClass = this.getContextClassLoader().loadClass("org.nbplugin.jpa.tools.DummyDataSource");
            this.getContextClassLoader().loadClass(this.dataSource.getDriverClass());
            Constructor dummyDataSourceConstructor = dummyDataSourceClass.getConstructor(String.class, String.class, String.class);
            Object dummyDataSource = dummyDataSourceConstructor.newInstance(this.dataSource.getUrl(), this.dataSource.getUsername(), this.dataSource.getPassword());
            Method getConnectionMethod = dummyDataSource.getClass().getMethod("getConnection");
            getConnectionMethod.invoke(dummyDataSource);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
