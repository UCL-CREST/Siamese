    public static Object deleteElements(Object array, int firstElement, int nElements) {
        if (nElements == 0 || array == null) return array;
        int oldLength = Array.getLength(array);
        if (oldLength - nElements <= 0) return array;
        Object t = Array.newInstance(array.getClass().getComponentType(), oldLength - nElements);
        if (firstElement > 0) System.arraycopy(array, 0, t, 0, firstElement);
        int n = oldLength - firstElement - nElements;
        if (n > 0) System.arraycopy(array, firstElement + nElements, t, firstElement, n);
        return t;
    }
