    public static Object deleteElements(Object array, int firstElement, int nElements) {
        if (nElements == 0 || array == null) return array;
        int oldLength = Array.getLength(array);
        if (firstElement >= oldLength) return array;
        int n = oldLength - (firstElement + nElements);
        if (n < 0) n = 0;
        Object t = Array.newInstance(array.getClass().getComponentType(), firstElement + n);
        if (firstElement > 0) System.arraycopy(array, 0, t, 0, firstElement);
        if (n > 0) System.arraycopy(array, firstElement + nElements, t, firstElement, n);
        return t;
    }
