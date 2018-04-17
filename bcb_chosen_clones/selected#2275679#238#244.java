    public static Object append(Object values, Object value) {
        if (!values.getClass().isArray()) throw new IllegalArgumentException("expected array type");
        Object[] result = (Object[]) Array.newInstance(values.getClass().getComponentType(), Array.getLength(values) + 1);
        System.arraycopy(values, 0, result, 0, result.length - 1);
        result[result.length - 1] = value;
        return result;
    }
