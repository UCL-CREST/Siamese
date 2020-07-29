    @SuppressWarnings({ "SuspiciousSystemArraycopy", "unchecked" })
    public static <T> T reallocArray(@NotNull T array, int length, boolean preserveData) {
        int current = Array.getLength(array);
        if (current == length) return array;
        T newArray = (T) Array.newInstance(array.getClass().getComponentType(), length);
        if (preserveData) {
            System.arraycopy(array, 0, newArray, 0, Math.min(length, current));
        }
        return newArray;
    }
