    private PrintAssembler create(Class assembler, OutputChannel outputChannel, Map<String, Object> attributes) throws PrintException {
        Constructor constructor;
        try {
            constructor = assembler.getConstructor(OutputChannel.class, Map.class);
        } catch (SecurityException e) {
            throw new PrintException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new PrintException(e.getMessage(), e);
        }
        try {
            return (PrintAssembler) constructor.newInstance(outputChannel, attributes);
        } catch (IllegalArgumentException e) {
            throw new PrintException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new PrintException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new PrintException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new PrintException(e.getMessage(), e);
        }
    }
