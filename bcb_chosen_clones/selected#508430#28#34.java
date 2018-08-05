    public static DiagramIface create(final String instanceName) {
        try {
            return (DiagramIface) Class.forName("com.c4j.diagram.DiagramImpl").getConstructor(Class.forName("java.lang.String")).newInstance(instanceName);
        } catch (Exception e) {
            throw new C4JRuntimeException(String.format("Could not create component instance ‘%s’.", instanceName), e);
        }
    }
