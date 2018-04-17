    public static Object[] join(Object[] arr1, Object[] arr2) {
        if (arr1 == null && arr2 != null) {
            return arr2;
        } else if (arr2 == null) {
            return arr1;
        } else {
            int size = arr1.length + arr2.length;
            Object[] arr = (Object[]) Array.newInstance(arr1.getClass().getComponentType(), size);
            System.arraycopy(arr1, 0, arr, 0, arr1.length);
            System.arraycopy(arr2, 0, arr, arr1.length, arr2.length);
            return arr;
        }
    }
