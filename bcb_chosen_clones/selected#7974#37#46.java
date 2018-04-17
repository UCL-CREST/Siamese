    @SuppressWarnings("unchecked")
    public void grow(int cap) {
        if (cap < data.length) throw new IllegalArgumentException("got cap " + cap + " when length was " + data.length);
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("growing to capacity " + cap);
        }
        T[] newData = (T[]) java.lang.reflect.Array.newInstance(data.getClass().getComponentType(), cap);
        System.arraycopy(data, 0, newData, 0, data.length);
        data = newData;
    }
