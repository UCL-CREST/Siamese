    @SuppressWarnings("unchecked")
    public static <T> T[] convertToArray(Object[] array, T[] typeSample) {
        if (typeSample.length < array.length) typeSample = (T[]) Array.newInstance(typeSample.getClass().getComponentType(), array.length);
        System.arraycopy(array, 0, typeSample, 0, array.length);
        if (typeSample.length > array.length) typeSample[array.length] = null;
        return typeSample;
    }
