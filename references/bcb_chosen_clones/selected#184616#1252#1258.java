    public static <T> T[] expand(T[] array, int amount) {
        Nulls.failIfNull(array, "Cannot expand a null array");
        int arrayLength = Array.getLength(array);
        @SuppressWarnings("unchecked") T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), arrayLength + amount);
        System.arraycopy(array, 0, newArray, 0, arrayLength);
        return newArray;
    }
