    public static Object rest(Object values) {
        if (!values.getClass().isArray()) throw new IllegalArgumentException("expected array type");
        Object[] result = (Object[]) Array.newInstance(values.getClass().getComponentType(), Array.getLength(values) - 1);
        System.arraycopy(values, 1, result, 0, result.length);
        return result;
    }
