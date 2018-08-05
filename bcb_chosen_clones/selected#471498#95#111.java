    @Override
    @SuppressWarnings("unchecked")
    protected EmbeddedTomcatServer createInstanceForValues(Object testObject, Class<?> testClass, List<String> values) {
        String version = values.get(0);
        boolean autoStart = Boolean.valueOf(values.get(1));
        String implClassName = "com.mtgi.test.unitils.tomcat." + version + ".EmbeddedTomcatServerImpl";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            Class<? extends EmbeddedTomcatServer> implClass = (Class<? extends EmbeddedTomcatServer>) loader.loadClass(implClassName);
            Constructor<? extends EmbeddedTomcatServer> con = implClass.getConstructor(File.class, Boolean.TYPE);
            return con.newInstance(IOUtils.createTempDir("embeddedTomcat"), autoStart);
        } catch (ClassNotFoundException e) {
            throw new UnitilsException("Unable to locate server class " + implClassName);
        } catch (Exception e) {
            throw new UnitilsException("Error initializing server class " + implClassName, e);
        }
    }
