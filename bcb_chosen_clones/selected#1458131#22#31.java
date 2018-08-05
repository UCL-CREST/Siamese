    public static Object expand(Object a) {
        Class cl = a.getClass();
        if (!cl.isArray()) return null;
        int length = Array.getLength(a);
        int newLength = length + 1;
        Class componentType = a.getClass().getComponentType();
        Object newArray = Array.newInstance(componentType, newLength);
        System.arraycopy(a, 0, newArray, 0, length);
        return newArray;
    }
