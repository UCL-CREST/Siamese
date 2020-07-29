    public static Object arrayGrow(Object array) {
        Class cl = array.getClass();
        if (!cl.isArray()) {
            return null;
        }
        Class componentType = cl.getComponentType();
        if (componentType.isArray()) {
            return null;
        }
        int length = Array.getLength(array);
        int newLength = length * 11 / 10 + 10;
        Object newArray = Array.newInstance(componentType, newLength);
        System.arraycopy(array, 0, newArray, 0, length);
        return newArray;
    }
