    public Object arrayGrow(Object a) {
        Class cl = a.getClass();
        if (!cl.isArray()) return null;
        Class componentType = a.getClass().getComponentType();
        int length = Array.getLength(a);
        int newLength = length + 1;
        Object newArray = Array.newInstance(componentType, newLength);
        System.arraycopy(a, 0, newArray, 0, length);
        return newArray;
    }
