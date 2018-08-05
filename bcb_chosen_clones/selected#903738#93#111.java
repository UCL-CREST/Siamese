    public static Object concat(Object toArray, int totalLen, Object[] arrs) {
        if (totalLen == 0) {
            return toArray;
        }
        if (totalLen > Array.getLength(toArray)) {
            toArray = Array.newInstance(toArray.getClass().getComponentType(), totalLen);
        }
        for (int i = 0, len = arrs.length, offset = 0; i < len; i++) {
            final Object arr = arrs[i];
            if (arr != null) {
                int arrayLen = Array.getLength(arr);
                if (arrayLen > 0) {
                    System.arraycopy(arr, 0, toArray, offset, arrayLen);
                    offset += arrayLen;
                }
            }
        }
        return toArray;
    }
