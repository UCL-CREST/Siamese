    @SuppressWarnings("unchecked")
    public static <T> T addarray(Class<?> type, T fieldobj, Object val) {
        Class<?> valtype = type.getComponentType();
        int index;
        if (fieldobj == null) {
            fieldobj = (T) Array.newInstance(valtype, 1);
            index = 0;
        } else {
            int len = Array.getLength(fieldobj);
            T newarray = (T) Array.newInstance(valtype, len + 1);
            System.arraycopy(fieldobj, 0, newarray, 0, len);
            fieldobj = newarray;
            index = len;
        }
        Array.set(fieldobj, index, val);
        return fieldobj;
    }
