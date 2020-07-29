    protected T getEvolver(final S l, final List<BoundaryCondition<S>> bcs) {
        try {
            return (T) classT.getConstructor(Operator.class, List.class).newInstance(l, bcs);
        } catch (final Exception e) {
            throw new LibraryException(e);
        }
    }
