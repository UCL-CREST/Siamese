    public Object[] toArray(Object array[]) {
        Object[] result = array;
        if (array.length > 0) {
            result = (Object[]) Array.newInstance(array.getClass().getComponentType(), 0);
        }
        result = collection.toArray(result);
        for (int i = 0; i < result.length; i++) {
            result[i] = new UnmodifiableEntry((Map.Entry) result[i]);
        }
        if (result.length > array.length) {
            return result;
        }
        System.arraycopy(result, 0, array, 0, result.length);
        if (array.length > result.length) {
            array[result.length] = null;
        }
        return array;
    }
