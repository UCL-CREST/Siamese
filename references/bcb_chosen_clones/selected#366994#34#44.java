    public static <E extends NamedObject> Set<E> createSet(final Class<E> pClass, final int pN) {
        final Set<E> list = new HashSet<E>(pN);
        for (int i = 0; i < pN; i++) {
            try {
                final E e = pClass.getConstructor(int.class).newInstance(i);
                list.add(e);
            } catch (Exception e) {
            }
        }
        return list;
    }
