    public static <T extends SqlTable.Implementation> T create(final Class<T> tableClass, final String alias) throws SQLEngineRuntimeException {
        try {
            final T t = tableClass.getConstructor(String.class).newInstance(alias);
            t.util.initialize();
            return t;
        } catch (final Exception e) {
            throw new SQLEngineRuntimeException(e);
        }
    }
