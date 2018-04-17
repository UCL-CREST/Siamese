    public void addValue(C value) {
        C[] newValues = (C[]) java.lang.reflect.Array.newInstance(values.getClass().getComponentType(), values.length + 1);
        System.arraycopy(values, 0, newValues, 0, values.length);
        newValues[values.length] = value;
        values = newValues;
        fireStateChanged();
    }
