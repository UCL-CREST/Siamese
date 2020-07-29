    public final Object[] toArray(final Object[] a) {
        if (a == null) {
            throw new NullPointerException("Array store cannot be null");
        }
        Object[] out;
        if (a.length < size) {
            out = (Object[]) Array.newInstance(a.getClass().getComponentType(), size);
        } else {
            for (int i = size; i < a.length; i++) {
                a[i] = null;
            }
            out = a;
        }
        System.arraycopy(toArray(), 0, out, 0, size);
        return out;
    }
