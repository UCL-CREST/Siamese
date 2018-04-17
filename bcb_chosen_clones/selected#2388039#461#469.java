    public static <T> T[] remove(T[] anArray, int anIndex) {
        T[] ret;
        if (anArray == null) return null; else {
            ret = (T[]) Array.newInstance(anArray.getClass().getComponentType(), anArray.length - 1);
        }
        System.arraycopy(anArray, 0, ret, 0, anIndex);
        System.arraycopy(anArray, anIndex + 1, ret, anIndex, anArray.length - anIndex - 1);
        return ret;
    }
