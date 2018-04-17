    public static <T extends SqlDdlTable.Implementation> T initializeFor(final DatabaseGateway<?> dbc, final Class<T> tableClass, final String alias) throws SQLEngineRuntimeException {
        try {
            final T t = tableClass.getConstructor(String.class).newInstance(alias);
            t.db().initializeFor(dbc);
            return t;
        } catch (final Exception e) {
            throw new SQLEngineRuntimeException(e);
        }
    }
