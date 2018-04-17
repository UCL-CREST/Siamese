    private IFile createIFile(Class cls, URI uri) throws Exception {
        Class[] parameterTypes = new Class[] { URI.class };
        Constructor constructor = cls.getConstructor(parameterTypes);
        Object[] initargs = new Object[] { uri };
        Object obj = constructor.newInstance(initargs);
        return (IFile) obj;
    }
