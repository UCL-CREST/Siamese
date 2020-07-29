    public static Object[] arrayInsert(Object[] source, int start, int count, Object value) {
        Object[] result = source;
        if (count > 0) {
            result = (Object[]) Array.newInstance(source.getClass().getComponentType(), source.length + count);
            if (start > 0) System.arraycopy(source, 0, result, 0, start);
            int rem = source.length - start;
            if (rem > 0) System.arraycopy(source, start, result, start + count, rem);
            for (int i = start; i < start + count; i++) result[i] = value;
        }
        return result;
    }
