    public static Object[] combine(Object[] a, Object[] b) {
        if (isEmpty(a) && isEmpty(b)) {
            if (a != null) {
                return (Object[]) Array.newInstance(a.getClass().getComponentType(), 0);
            } else if (b != null) {
                return (Object[]) Array.newInstance(b.getClass().getComponentType(), 0);
            } else {
                return new Object[0];
            }
        } else if (isEmpty(a)) {
            return b;
        } else if (isEmpty(b)) {
            return a;
        } else {
            Object[] newArray = null;
            newArray = (Object[]) Array.newInstance(a.getClass().getComponentType(), a.length + b.length);
            System.arraycopy(a, 0, newArray, 0, a.length);
            System.arraycopy(b, 0, newArray, a.length, b.length);
            return newArray;
        }
    }
