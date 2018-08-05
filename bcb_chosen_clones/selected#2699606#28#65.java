    private static void prepare() {
        System.err.println("PREPARING-----------------------------------------");
        deleteHome();
        InputStream configStream = null;
        FileOutputStream tempStream = null;
        try {
            configStream = AllTests.class.getClassLoader().getResourceAsStream("net/sf/archimede/test/resources/repository.xml");
            new File("temp").mkdir();
            tempStream = new FileOutputStream(new File("temp/repository.xml"));
            IOUtils.copy(configStream, tempStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (configStream != null) {
                    configStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (tempStream != null) {
                    try {
                        tempStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        String repositoryName = "jackrabbit.repository";
        Properties jndiProperties = new Properties();
        jndiProperties.put("java.naming.provider.url", "http://sf.net/projects/archimede#1");
        jndiProperties.put("java.naming.factory.initial", "org.apache.jackrabbit.core.jndi.provider.DummyInitialContextFactory");
        startupUtil = new StartupJcrUtil(REPOSITORY_HOME, "temp/repository.xml", repositoryName, jndiProperties);
        startupUtil.init();
    }
