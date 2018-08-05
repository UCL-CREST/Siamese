    @SuppressWarnings("unchecked")
    public void grow(int cap) {
        if (cap < data.length) throw new IllegalArgumentException("got cap " + cap + " when length was " + data.length);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("growing to capacity " + cap);
        }
        T[][] newData = (T[][]) java.lang.reflect.Array.newInstance(data.getClass().getComponentType(), cap);
        int i = 0;
        for (T[] a : data) {
            T[] newA = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), cap);
            System.arraycopy(a, 0, newA, 0, a.length);
            newData[i++] = newA;
        }
        data = newData;
    }
