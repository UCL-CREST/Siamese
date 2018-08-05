    public Object[] toArray(Object a[]) {
        if (a.length < size) a = (Object[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        if (head < tail) {
            System.arraycopy(elementData, head, a, 0, tail - head);
        } else {
            System.arraycopy(elementData, head, a, 0, elementData.length - head);
            System.arraycopy(elementData, 0, a, elementData.length - head, tail);
        }
        if (a.length > size) a[size] = null;
        return a;
    }
