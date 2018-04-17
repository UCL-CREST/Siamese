    public void reset() {
        _environmentDelegate.reset();
        ITokenCollection tokenCollection = getTokenCollection();
        tokenCollection.clear();
        int size = getFileCount();
        String name = "0";
        int value = 0;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                IntegerPosition position = IntegerPosition.get(x, y);
                try {
                    Constructor<Token> constructor = Token.class.getConstructor(new Class[] { IPosition.class, String.class, int.class });
                    Token token = constructor.newInstance(new Object[] { position, name, value });
                    tokenCollection.add(token);
                } catch (Exception e) {
                    throw new VizziniRuntimeException(e);
                }
            }
        }
    }
