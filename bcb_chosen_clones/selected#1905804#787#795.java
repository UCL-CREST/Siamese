    public Object[] toArray(Object a[]) {
        a = (Object[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), getSize());
        int indexLocations = 0;
        for (int x = 0; x < cells.length; x++) {
            System.arraycopy(cells[x], 0, a, indexLocations, cells[0].length);
            indexLocations += cells[0].length;
        }
        return a;
    }
