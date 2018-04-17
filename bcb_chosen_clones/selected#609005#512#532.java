    @SuppressWarnings("unchecked")
    public static <T> T[] insert(T list[], T obj, int index) {
        if (list != null) {
            int ndx = ((index > list.length) || (index < 0)) ? list.length : index;
            Class type = list.getClass().getComponentType();
            int size = (list.length > ndx) ? (list.length + 1) : (ndx + 1);
            T array[] = (T[]) Array.newInstance(type, size);
            if (ndx > 0) {
                int maxLen = (list.length >= ndx) ? ndx : list.length;
                System.arraycopy(list, 0, array, 0, maxLen);
            }
            array[ndx] = obj;
            if (ndx < list.length) {
                int maxLen = list.length - ndx;
                System.arraycopy(list, ndx, array, ndx + 1, maxLen);
            }
            return array;
        } else {
            return null;
        }
    }
