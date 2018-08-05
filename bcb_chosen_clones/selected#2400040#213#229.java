    @NotNull
    public static <T> T[] concat(@NotNull final T[]... a) {
        int ns = 0;
        for (final T[] anA : a) {
            if (anA != null) {
                ns += anA.length;
            }
        }
        final T[] na = (T[]) Array.newInstance(a[0].getClass().getComponentType(), ns);
        int np = 0;
        for (final T[] anA : a) {
            System.arraycopy(anA, 0, na, np, anA.length);
            np += anA.length;
        }
        assert ns == np;
        return na;
    }
