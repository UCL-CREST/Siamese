    public static <T> T[] add(T[] anArray, T anElement, int anIndex) {
        T[] ret;
        if (anArray == null) {
            if (anIndex != 0) throw new ArrayIndexOutOfBoundsException("Cannot set " + anIndex + " element in a null array");
            ret = (T[]) Array.newInstance(anElement.getClass(), 1);
            ret[0] = anElement;
            return ret;
        }
        ret = (T[]) Array.newInstance(anArray.getClass().getComponentType(), anArray.length + 1);
        System.arraycopy(anArray, 0, ret, 0, anIndex);
        put(ret, anElement, anIndex);
        System.arraycopy(anArray, anIndex, ret, anIndex + 1, anArray.length - anIndex);
        return ret;
    }
