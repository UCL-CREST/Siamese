    public static Object[] prepend(Object[] oldArray, Object o) {
        NullArgumentException.check(oldArray);
        NullArgumentException.check(o);
        Object[] newArray = (Object[]) Array.newInstance(oldArray.getClass().getComponentType(), oldArray.length + 1);
        System.arraycopy(oldArray, 0, newArray, 1, oldArray.length);
        newArray[0] = o;
        return newArray;
    }
