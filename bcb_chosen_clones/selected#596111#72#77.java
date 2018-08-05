    public int[] toArray(int a[]) {
        if (a.length < size) a = (int[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size) a[size] = 0;
        return a;
    }
