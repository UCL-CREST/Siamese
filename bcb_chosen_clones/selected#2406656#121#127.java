    public ArrayBasedSet(E[] A, Comparator<E> comparator) {
        this.comparator = comparator;
        type = (Class<E>) A.getClass().getComponentType();
        array = (E[]) java.lang.reflect.Array.newInstance(type, A.length);
        System.arraycopy(A, 0, array, 0, A.length);
        size = A.length;
    }
