    @SuppressWarnings("unchecked")
    public static <T> T[] removeCallbackFromList(T[] curList, int index) {
        final int curLength = curList.length;
        assert (index >= 0 && index < curLength);
        if (curLength == 1) {
            return null;
        }
        final int newLength = curLength - 1;
        T[] newList = (T[]) Array.newInstance(curList.getClass().getComponentType(), newLength);
        System.arraycopy(curList, 0, newList, 0, index);
        System.arraycopy(curList, index + 1, newList, index, newLength - index);
        return newList;
    }
