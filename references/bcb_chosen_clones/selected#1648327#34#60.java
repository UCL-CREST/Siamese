    public static Object mergeArrays(Object arr1, Object arr2) {
        if (arr1 == null && arr2 == null) {
            throw new IllegalArgumentException("Both arr1 and arr2 may not be null");
        }
        Class<?> type1 = arr1 != null ? arr1.getClass().getComponentType() : null;
        if (arr1 != null && type1 == null) {
            throw new IllegalArgumentException("Argument arr1 of type " + arr1.getClass().getName() + " is not an array");
        }
        Class<?> type2 = arr2 != null ? arr2.getClass().getComponentType() : null;
        if (arr2 != null && type2 == null) {
            throw new IllegalArgumentException("Argument arr2 of type " + arr2.getClass().getName() + " is not an array");
        }
        if (arr1 != null && arr2 != null && !type1.equals(type2)) {
            throw new IllegalArgumentException("Incompatible array types: " + arr1.getClass().getComponentType().getName() + " and " + arr2.getClass().getComponentType().getName());
        }
        Class<?> type = arr1 != null ? type1 : type2;
        int len1 = arr1 != null ? Array.getLength(arr1) : 0;
        int len2 = arr2 != null ? Array.getLength(arr2) : 0;
        Object merged = Array.newInstance(type, len1 + len2);
        if (arr1 != null) {
            System.arraycopy(arr1, 0, merged, 0, len1);
        }
        if (arr2 != null) {
            System.arraycopy(arr2, 0, merged, len1, len2);
        }
        return merged;
    }
