    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] targetArray) {
        if (targetArray.length < used) {
            targetArray = (T[]) java.lang.reflect.Array.newInstance(targetArray.getClass().getComponentType(), used);
        }
        System.arraycopy(arr, 0, targetArray, 0, used);
        if (targetArray.length > used) {
            targetArray[used] = null;
        }
        return targetArray;
    }
