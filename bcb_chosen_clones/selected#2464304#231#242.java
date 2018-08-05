    public final Object getFieldValue(String fieldName) {
        checkIllegalFieldName(fieldName);
        int i = fieldIndex(fieldName);
        if (i < 0) return null;
        Object v = values[i];
        if (v == null || !v.getClass().isArray()) return v;
        if (v instanceof Object[]) return ((Object[]) v).clone();
        int len = Array.getLength(v);
        Object a = Array.newInstance(v.getClass().getComponentType(), len);
        System.arraycopy(v, 0, a, 0, len);
        return a;
    }
