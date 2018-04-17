    public Object toArray(Object a) {
        if (Array.getLength(a) < elementCount) {
            a = Array.newInstance(a.getClass().getComponentType(), elementCount);
        }
        System.arraycopy(elementData, 0, a, 0, elementCount);
        return a;
    }
