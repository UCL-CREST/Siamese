    public static Object[] add(final Object[] a, final Object[] b) {
        if (a != null && b != null) {
            if (a.length != 0 && b.length != 0) {
                final Object[] array = (Object[]) Array.newInstance(a.getClass().getComponentType(), a.length + b.length);
                System.arraycopy(a, 0, array, 0, a.length);
                System.arraycopy(b, 0, array, a.length, b.length);
                return array;
            } else if (b.length == 0) {
                return a;
            } else {
                return b;
            }
        } else if (b == null) {
            return a;
        } else {
            return b;
        }
    }
