    public synchronized SUTConnection getConnection(String name) {
        Object val = sutcTable.get(name);
        if (val instanceof Class<?>) {
            try {
                Constructor<?> c = ((Class<?>) val).getConstructor(new Class[] { String.class });
                val = c.newInstance(new Object[] { name });
                sutcTable.put(name, val);
            } catch (Throwable t) {
                sutcTable.remove(name);
                LOGGER.logError(t);
            }
        }
        return (SUTConnection) val;
    }
