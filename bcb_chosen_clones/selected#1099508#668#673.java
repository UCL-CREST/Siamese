    private static Object[] append(Object[] orig, Object item) {
        Object[] newArr = (Object[]) java.lang.reflect.Array.newInstance(orig.getClass().getComponentType(), orig.length + 1);
        System.arraycopy(orig, 0, newArr, 0, orig.length);
        newArr[orig.length] = item;
        return newArr;
    }
