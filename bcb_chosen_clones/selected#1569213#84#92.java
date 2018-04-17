    public void removeValue(int index) {
        if (index >= 0 && index < values.length) {
            C[] newValues = (C[]) Array.newInstance(values.getClass().getComponentType(), values.length - 1);
            if (index >= 1) System.arraycopy(values, 0, newValues, 0, index);
            if (index <= values.length - 2) System.arraycopy(values, index + 1, newValues, index, values.length - index - 1);
            values = newValues;
            fireStateChanged();
        }
    }
