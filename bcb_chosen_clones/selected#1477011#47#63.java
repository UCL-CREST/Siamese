    protected Bin createNewBin(Class binImpl, String id, Size capacity) throws BinCreationException {
        Constructor constructor;
        try {
            constructor = binImpl.getConstructor(CONSTRUCTOR_PARAM_CLASSES);
        } catch (NoSuchMethodException e) {
            throw new BinCreationException("Bin impl '" + binImpl.getName() + "' does not provide a constructor with the following parameters " + Arrays.asList(CONSTRUCTOR_PARAM_CLASSES), e);
        }
        try {
            return (Bin) constructor.newInstance(new Object[] { id, capacity });
        } catch (InstantiationException e) {
            throw new BinCreationException("Could not instantiate bin impl '" + binImpl.getName() + "'", e);
        } catch (IllegalAccessException e) {
            throw new BinCreationException("Could not instantiate bin impl '" + binImpl.getName() + "'", e);
        } catch (InvocationTargetException e) {
            throw new BinCreationException("Could not instantiate bin impl '" + binImpl.getName() + "'", e);
        }
    }
