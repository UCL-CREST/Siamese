    public static Object addP(Object anArray, Object anElement, int anIndex) {
        Object ret;
        int length;
        if (anArray == null) {
            if (anIndex != 0) throw new ArrayIndexOutOfBoundsException("Cannot set " + anIndex + " element in a null array");
            ret = Array.newInstance(anElement.getClass(), 1);
            Array.set(ret, 0, anElement);
            return ret;
        } else {
            length = Array.getLength(anArray);
            ret = Array.newInstance(anArray.getClass().getComponentType(), length + 1);
        }
        System.arraycopy(anArray, 0, ret, 0, anIndex);
        put(ret, anElement, anIndex);
        System.arraycopy(anArray, anIndex, ret, anIndex + 1, length - anIndex);
        return ret;
    }
