    public static Object concat(Object arr1, Object arr2) {
        int len1 = (arr1 == null) ? (-1) : Array.getLength(arr1);
        if (len1 <= 0) {
            return arr2;
        }
        int len2 = (arr2 == null) ? (-1) : Array.getLength(arr2);
        if (len2 <= 0) {
            return arr1;
        }
        Class commonComponentType = commonClass(arr1.getClass().getComponentType(), arr2.getClass().getComponentType());
        Object newArray = Array.newInstance(commonComponentType, len1 + len2);
        System.arraycopy(arr1, 0, newArray, 0, len1);
        System.arraycopy(arr2, 0, newArray, len1, len2);
        return newArray;
    }
