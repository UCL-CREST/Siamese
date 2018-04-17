    public Object[] toArray(Object[] a) {
        Class t = a.getClass().getComponentType();
        if ((t != Object.class) & (t != Rectangle.class)) {
            for (int i = 0; i < a.length; i++) a[i] = null;
            return a;
        }
        if (a.length < size) a = new Rectangle[size];
        System.arraycopy(rects, 0, a, 0, size);
        for (int i = size; i < a.length; i++) a[i] = null;
        return a;
    }
