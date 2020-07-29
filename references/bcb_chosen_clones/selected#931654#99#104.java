    public void compact() {
        if (addIndex == size()) return;
        Object narray = Array.newInstance(array.getClass().getComponentType(), addIndex);
        System.arraycopy(array, 0, narray, 0, addIndex);
        array = narray;
    }
