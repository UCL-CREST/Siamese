    public static <T> T[] add(T[] anArray, int anIndex, T... elements) {
        T[] ret;
        if (anArray == null) {
            if (anIndex != 0) throw new ArrayIndexOutOfBoundsException("Cannot set " + anIndex + " element in a null array");
            return elements;
        }
        ret = (T[]) Array.newInstance(anArray.getClass().getComponentType(), anArray.length + elements.length);
        System.arraycopy(anArray, 0, ret, 0, anIndex);
        System.arraycopy(elements, 0, ret, anIndex, elements.length);
        System.arraycopy(anArray, anIndex, ret, anIndex + elements.length, anArray.length - anIndex);
        return ret;
    }
