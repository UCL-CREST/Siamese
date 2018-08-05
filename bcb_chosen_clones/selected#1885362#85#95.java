    @SuppressWarnings("unchecked")
    public static <T> T[] remove(T[] oldArray, int idx) {
        if (idx < 0 || idx >= oldArray.length) throw new IllegalArgumentException("array index " + idx + " out of bounds");
        Class<?> component = oldArray.getClass().getComponentType();
        T[] array = (T[]) Array.newInstance(component, oldArray.length - 1);
        if (idx == 0) System.arraycopy(oldArray, 1, array, 0, array.length); else {
            System.arraycopy(oldArray, 0, array, 0, idx);
            System.arraycopy(oldArray, idx + 1, array, idx, array.length - idx);
        }
        return array;
    }
