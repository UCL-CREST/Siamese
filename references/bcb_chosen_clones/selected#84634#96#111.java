    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] appendToThis, T[] these) {
        if (appendToThis == null) {
            throw new NullPointerException("attempt to append to a null array");
        }
        if (these == null) {
            throw new NullPointerException("attempt to append a null array");
        }
        T[] result;
        int newSize = appendToThis.length + these.length;
        Class<?> componentType = appendToThis.getClass().getComponentType();
        result = (T[]) Array.newInstance(componentType, newSize);
        System.arraycopy(appendToThis, 0, result, 0, appendToThis.length);
        System.arraycopy(these, 0, result, appendToThis.length, these.length);
        return result;
    }
