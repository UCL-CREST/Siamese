    public static Object removeP(Object anArray, int anIndex) {
        Object ret;
        int length;
        if (anArray == null) return null; else {
            length = Array.getLength(anArray);
            ret = Array.newInstance(anArray.getClass().getComponentType(), length - 1);
        }
        System.arraycopy(anArray, 0, ret, 0, anIndex);
        System.arraycopy(anArray, anIndex + 1, ret, anIndex, length - anIndex - 1);
        return ret;
    }
