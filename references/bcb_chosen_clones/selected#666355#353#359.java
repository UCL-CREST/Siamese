    public static FilterOutputStream newOuputFilter(Class<? extends FilterOutputStream> filter, OutputStream delegate) {
        try {
            return filter.getConstructor(OutputStream.class).newInstance(delegate);
        } catch (Exception e) {
            throw AoThrowables.wrap(e);
        }
    }
