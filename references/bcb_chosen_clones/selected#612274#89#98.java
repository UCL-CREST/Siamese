    public Object[] toArray(Object a[]) {
        if (a.length < elements.length) {
            a = (Object[]) Array.newInstance(a.getClass().getComponentType(), elements.length);
        }
        System.arraycopy(elements, 0, a, 0, elements.length);
        if (a.length > elements.length) {
            a[elements.length] = null;
        }
        return a;
    }
