    static Object[] toArray(Object[] to, Object[] data, int start, int size) {
        int l = data.length;
        if (to.length < l) {
            to = (Object[]) Array.newInstance(to.getClass().getComponentType(), l);
        } else {
            if (to.length > l) {
                to[l] = null;
            }
        }
        System.arraycopy(data, start, to, 0, size);
        return to;
    }
