    private static void initStaticContext() throws ApplicationContextException {
        try {
            String contextClassName = System.getProperty(SYSTEM_PROPERTY_CONTEXT_CLASS);
            if (contextClassName == null) {
                context = new XMLApplicationContext(System.getProperties());
            } else {
                Class contextClass = ClassUtils.forName(contextClassName);
                Constructor constructor = contextClass.getConstructor(new Class[] { Map.class });
                context = (ApplicationContext) constructor.newInstance(new Object[] { System.getProperties() });
            }
        } catch (InvocationTargetException e) {
            log.error("failed to create ApplicationContext", e.getTargetException());
            throw new ApplicationContextException("failed to create ApplicationContext", e.getTargetException());
        } catch (Throwable t) {
            log.error("failed to create ApplicationContext", t);
            throw new ApplicationContextException("failed to create ApplicationContext", t);
        }
    }
