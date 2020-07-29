    public Object createContext(Object param) throws Exception {
        return getConstructor().newInstance(new Object[] { new TSContextFactory(param) });
    }
