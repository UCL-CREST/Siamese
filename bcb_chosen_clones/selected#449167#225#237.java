    @SuppressWarnings("unchecked")
    private static <T> T[] prepareIndexedArray(Class<T> collectionType, Object existingCollection, Object collectionEntry, int index) {
        T[] result;
        if (existingCollection == null) {
            result = (T[]) Array.newInstance(collectionType.getComponentType(), index + 1);
        } else {
            int originalLenth = ((Object[]) existingCollection).length;
            result = (T[]) Array.newInstance(collectionType.getComponentType(), Math.max(index + 1, originalLenth));
            System.arraycopy(existingCollection, 0, result, 0, originalLenth);
        }
        result[index] = (T) collectionEntry;
        return result;
    }
