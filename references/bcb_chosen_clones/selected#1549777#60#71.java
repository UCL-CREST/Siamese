    @SuppressWarnings("unchecked")
    public static <T> Symbol<T> newSymbolInstance(Class<? extends Symbol<T>> cls, SymbolType<T> type, char c) {
        try {
            Constructor<? extends Symbol<T>> ct = cls.getConstructor(char.class);
            Object retobj = ct.newInstance(c);
            ((AbstractSymbol<T>) retobj).type = type;
            return (AbstractSymbol<T>) retobj;
        } catch (Throwable e) {
            System.err.println(e);
        }
        return null;
    }
