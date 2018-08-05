    static Object arrayExpand(Object a, int amount) {
        Class cl = a.getClass();
        if (!cl.isArray()) return null;
        int length = Array.getLength(a);
        int newLength = length + amount;
        Class componentType = a.getClass().getComponentType();
        Object newArray = Array.newInstance(componentType, newLength);
        System.arraycopy(a, 0, newArray, 0, length);
        return newArray;
    }
