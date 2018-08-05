    public static <T> T remove(T array, int idx, int length) {
        Class componentType = array.getClass().getComponentType();
        int originalLength = Array.getLength(array);
        T newarray = (T) Array.newInstance(componentType, originalLength - length);
        if (idx > 0) System.arraycopy(array, 0, newarray, 0, idx);
        if (idx + length < originalLength) System.arraycopy(array, idx + length, newarray, idx, originalLength - (idx + length));
        return newarray;
    }
