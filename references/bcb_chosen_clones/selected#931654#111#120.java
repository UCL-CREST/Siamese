    public void add(Object value) {
        int n = size();
        if (addIndex >= n) {
            if (n == 0) n = 8; else n *= 2;
            Object narray = Array.newInstance(array.getClass().getComponentType(), n);
            System.arraycopy(array, 0, narray, 0, addIndex);
            array = narray;
        }
        Array.set(array, addIndex++, value);
    }
