    public Object getObject(String inputClassName) throws ClassInstantiationException {
        FlatFileIndexWriter indexHelper = null;
        try {
            Class analyzerClass = Class.forName(inputClassName);
            Constructor constructor = analyzerClass.getConstructor(new Class[] {});
            indexHelper = (FlatFileIndexWriter) constructor.newInstance(new Object[] {});
        } catch (Throwable e) {
            throw new ClassInstantiationException(inputClassName);
        }
        return indexHelper;
    }
