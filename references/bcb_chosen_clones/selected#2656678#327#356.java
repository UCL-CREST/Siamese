    @SuppressWarnings("rawtypes")
    public Object remove(Object arrayOrCollection, Object item) {
        if (arrayOrCollection == null) {
            throw new NlsIllegalArgumentException(null);
        }
        Class<?> type = arrayOrCollection.getClass();
        if (type.isArray()) {
            int size = Array.getLength(arrayOrCollection);
            for (int index = 0; index < size; index++) {
                Object currentItem = Array.get(arrayOrCollection, index);
                if ((item == currentItem) || ((item != null) && (item.equals(currentItem)))) {
                    Object newArray = Array.newInstance(type.getComponentType(), size - 1);
                    System.arraycopy(arrayOrCollection, 0, newArray, 0, index);
                    System.arraycopy(arrayOrCollection, index + 1, newArray, index, size - index - 1);
                    return newArray;
                }
            }
            return null;
        } else if (Collection.class.isAssignableFrom(type)) {
            Collection collection = (Collection) arrayOrCollection;
            boolean removed = collection.remove(item);
            if (removed) {
                return arrayOrCollection;
            } else {
                return null;
            }
        } else {
            throw new NlsIllegalArgumentException(arrayOrCollection);
        }
    }
