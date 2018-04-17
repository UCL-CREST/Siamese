    @SuppressWarnings("unchecked")
    protected <T> T[] add(T[] objs, T value, Class<T> clazz) {
        T[] newObjs;
        if (objs == null) {
            newObjs = (T[]) Array.newInstance(clazz, 1);
            newObjs[0] = value;
        } else {
            newObjs = (T[]) Array.newInstance(objs.getClass().getComponentType(), objs.length + 1);
            System.arraycopy(objs, 0, newObjs, 0, objs.length);
            newObjs[objs.length] = value;
        }
        return newObjs;
    }
