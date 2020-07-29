    @SuppressWarnings("unchecked")
    private static <T> T[] appendToArray(T[] listeners, T newElement) {
        int length = listeners.length;
        T[] ret = (T[]) java.lang.reflect.Array.newInstance(listeners.getClass().getComponentType(), length + 1);
        System.arraycopy(listeners, 0, ret, 0, length);
        ret[length] = newElement;
        return ret;
    }
