    public static Object[] addObjectToArray(Object[] array, Object object) {
        Class<?> compType = Object.class;
        if (array != null) {
            compType = array.getClass().getComponentType();
        } else if (object != null) {
            compType = object.getClass();
        }
        int newArrLength = (array != null ? array.length + 1 : 1);
        Object[] newArr = (Object[]) Array.newInstance(compType, newArrLength);
        if (array != null) {
            System.arraycopy(array, 0, newArr, 0, array.length);
        }
        newArr[newArr.length - 1] = object;
        return newArr;
    }
