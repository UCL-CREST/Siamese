    public static Object[] insert(Object[] objectArray, Object o, int index) {
        NullArgumentException.check(objectArray);
        NullArgumentException.check(o);
        if ((index < 0) || (index > objectArray.length)) {
            throw new IndexOutOfBoundsException("index out of range:  " + index);
        }
        Object[] newObjectArray = (Object[]) Array.newInstance(objectArray.getClass().getComponentType(), objectArray.length + 1);
        System.arraycopy(objectArray, 0, newObjectArray, 0, index);
        newObjectArray[index] = o;
        System.arraycopy(objectArray, index, newObjectArray, index + 1, objectArray.length - index);
        return newObjectArray;
    }
