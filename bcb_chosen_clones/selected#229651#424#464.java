    public static ArrayDataSet append(ArrayDataSet ths, ArrayDataSet ds) {
        if (ds.rank() != ths.rank) throw new IllegalArgumentException("rank mismatch");
        if (ds.len1 != ths.len1) throw new IllegalArgumentException("len1 mismatch");
        if (ds.len2 != ths.len2) throw new IllegalArgumentException("len2 mismatch");
        if (ds.len3 != ths.len3) throw new IllegalArgumentException("len3 mismatch");
        if (ths.getBack().getClass() != ds.getBack().getClass()) throw new IllegalArgumentException("backing type mismatch");
        int myLength = ths.len0 * ths.len1 * ths.len2 * ths.len3;
        int dsLength = ds.len0 * ds.len1 * ds.len2 * ds.len3;
        Object newback = Array.newInstance(ths.getBack().getClass().getComponentType(), myLength + dsLength);
        System.arraycopy(ths.getBack(), 0, newback, 0, myLength);
        System.arraycopy(ds.getBack(), 0, newback, myLength, dsLength);
        Units u1 = SemanticOps.getUnits(ths);
        Units u2 = SemanticOps.getUnits(ds);
        if (u1 != u2) {
            UnitsConverter uc = UnitsConverter.getConverter(u2, u1);
            Class backClass = ths.getBack().getClass().getComponentType();
            for (int i = myLength; i < myLength + dsLength; i++) {
                Number nv = uc.convert(Array.getDouble(newback, i));
                if (backClass == double.class) {
                    Array.set(newback, i, nv.doubleValue());
                } else if (backClass == float.class) {
                    Array.set(newback, i, nv.floatValue());
                } else if (backClass == long.class) {
                    Array.set(newback, i, nv.longValue());
                } else if (backClass == int.class) {
                    Array.set(newback, i, nv.intValue());
                } else if (backClass == short.class) {
                    Array.set(newback, i, nv.shortValue());
                } else if (backClass == byte.class) {
                    Array.set(newback, i, nv.byteValue());
                } else {
                    throw new IllegalArgumentException("unsupported type: " + backClass);
                }
            }
        }
        int len0 = ths.len0 + ds.len0;
        ArrayDataSet result = create(ths.rank, len0, ths.len1, ths.len2, ths.len3, newback);
        result.properties.putAll(joinProperties(ths, ds));
        result.properties.put(QDataSet.UNITS, u1);
        return result;
    }
