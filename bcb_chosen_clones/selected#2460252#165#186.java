    protected Resource.Factory resolveTextResourceFactory() throws SimTLException {
        String olS = findTemplateAnnotation().getDetails().get(ANNOTATION_OBJECT_LANGUAGE_FACTORY);
        if (olS == null) {
            throw new InjectionException("No \"" + ANNOTATION_OBJECT_LANGUAGE_FACTORY + "\" annotation details at annotation " + ANNOTATION_TEMPLATE_CLASS);
        }
        Class<?> factoryClass = null;
        try {
            factoryClass = Class.forName(olS);
        } catch (ClassNotFoundException e) {
            throw new ValidationException("No such class found:" + olS + ". Please make this language available", e);
        }
        Object factoryO = null;
        try {
            factoryO = factoryClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new InjectionException("Couldn't instantiate with empty constructor. Please make this language available", e);
        }
        if (!(factoryO instanceof Resource.Factory)) {
            throw new InjectionException("Referenced class is no ResourceFactory: " + olS);
        }
        return (Resource.Factory) factoryO;
    }
