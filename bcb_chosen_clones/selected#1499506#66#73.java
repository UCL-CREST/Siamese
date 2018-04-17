    public static Object[] append(Object[] oldArray, Object o) {
        NullArgumentException.check(oldArray);
        NullArgumentException.check(o);
        Object[] newArray = (Object[]) Array.newInstance(oldArray.getClass().getComponentType(), oldArray.length + 1);
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[oldArray.length] = o;
        return newArray;
    }
