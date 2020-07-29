    @SuppressWarnings("unchecked")
    public static <T> T[] add(T[] array, T element) {
        final T[] result;
        if (array != null) {
            result = Arrays.createArray((Class<? extends T>) array.getClass().getComponentType(), array.length + 1);
            System.arraycopy(array, 0, result, 0, array.length);
        } else {
            Validations.isTrue(element != null, "Both array and element are null");
            assert element != null;
            result = Arrays.createArray((Class<? extends T>) element.getClass(), 1);
        }
        result[result.length - 1] = element;
        return result;
    }
