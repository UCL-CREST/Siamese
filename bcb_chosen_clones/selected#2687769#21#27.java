    @SuppressWarnings("unchecked")
    public static final Module instantiateModule(String className, ModuleCommunicationLine moduleCommunicationLine, JWAIMLogger logger) throws Exception {
        Class c = Class.forName(className);
        Constructor cons = c.getConstructor(ModuleCommunicationLine.class, JWAIMLogger.class);
        Object cf = cons.newInstance(moduleCommunicationLine, logger);
        return (Module) cf;
    }
