    public static FilterInputStream newInputFilter(Class<? extends FilterInputStream> filter, InputStream delegate) {
        try {
            return filter.getConstructor(InputStream.class).newInstance(delegate);
        } catch (Exception e) {
            throw AoThrowables.wrap(e);
        }
    }
