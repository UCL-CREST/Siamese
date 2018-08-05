    @SuppressWarnings("unchecked")
    public static <T> T[] trim(T[] array, int newSize) {
        Assert.notNull(array);
        Assert.inRange(newSize, 0, Integer.MAX_VALUE, "newSize may not be smaller than zero");
        if (array.length > newSize) {
            Class<?> type = array.getClass().getComponentType();
            T[] newArray = (T[]) Array.newInstance(type, newSize);
            System.arraycopy(array, 0, newArray, 0, newSize);
            return newArray;
        } else return array;
    }
