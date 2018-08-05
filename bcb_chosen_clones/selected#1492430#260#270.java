    public static <T> T[] addWithoutDuplicates(T[] values, T newValue) {
        for (T value : values) {
            if (value.equals(newValue)) {
                return values;
            }
        }
        T[] largerOne = (T[]) Array.newInstance(values.getClass().getComponentType(), values.length + 1);
        System.arraycopy(values, 0, largerOne, 0, values.length);
        largerOne[values.length] = newValue;
        return largerOne;
    }
