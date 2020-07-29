    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object add(Object arrayOrCollection, Object item) {
        if (arrayOrCollection == null) {
            throw new NlsIllegalArgumentException(null);
        }
        Class<?> type = arrayOrCollection.getClass();
        if (type.isArray()) {
            int size = Array.getLength(arrayOrCollection);
            Object newArray = Array.newInstance(type.getComponentType(), size + 1);
            System.arraycopy(arrayOrCollection, 0, newArray, 0, size);
            Array.set(newArray, size, item);
            return newArray;
        } else if (Collection.class.isAssignableFrom(type)) {
            Collection collection = (Collection) arrayOrCollection;
            collection.add(item);
        } else {
            throw new NlsIllegalArgumentException(arrayOrCollection);
        }
        return arrayOrCollection;
    }
