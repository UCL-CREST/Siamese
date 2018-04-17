    @SuppressWarnings("unchecked")
    public static <T> T[] remove(T list[], int ndx) {
        if ((list != null) && (ndx >= 0) && (ndx < list.length)) {
            Class type = list.getClass().getComponentType();
            T array[] = (T[]) Array.newInstance(type, list.length - 1);
            if (ndx > 0) {
                System.arraycopy(list, 0, array, 0, ndx);
            }
            if (ndx < (list.length - 1)) {
                System.arraycopy(list, ndx + 1, array, ndx, list.length - ndx - 1);
            }
            return array;
        } else {
            return null;
        }
    }
