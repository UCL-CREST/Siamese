    public static TasksIface create(final String instanceName) {
        try {
            return (TasksIface) Class.forName("com.c4j.tasks.TasksImpl").getConstructor(Class.forName("java.lang.String")).newInstance(instanceName);
        } catch (Exception e) {
            throw new C4JRuntimeException(String.format("Could not create component instance ‘%s’.", instanceName), e);
        }
    }
