    public static Object[] append(Object[] lst, Object[] lst1) {
        Object[] l = (Object[]) Array.newInstance(lst.getClass().getComponentType(), lst.length + lst1.length);
        System.arraycopy(lst, 0, l, 0, lst.length);
        System.arraycopy(lst1, 0, l, lst.length, lst1.length);
        return l;
    }
