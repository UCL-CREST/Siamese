    public static <E extends NamedObject> List<E> createList(final Class<E> pClass, final int pN) {
        final List<E> list = new ArrayList<E>(pN);
        for (int i = 0; i < pN; i++) {
            try {
                final E e = pClass.getConstructor(int.class).newInstance(i);
                list.add(e);
            } catch (Exception e) {
            }
        }
        return list;
    }
