    @SuppressWarnings("unchecked")
    static <T, U> T[] copyOfRange(U[] original, int start, int end, Class<? extends T[]> newType) {
        if (start <= end) {
            if (original.length >= start && 0 <= start) {
                int length = end - start;
                int copyLength = Math.min(length, original.length - start);
                T[] copy = (T[]) Array.newInstance(newType.getComponentType(), length);
                System.arraycopy(original, start, copy, 0, copyLength);
                return copy;
            }
            throw new ArrayIndexOutOfBoundsException();
        }
        throw new IllegalArgumentException();
    }
