    public static Object expandCollection(Object collection, int size) {
        if (collection == null) {
            return null;
        }
        if (size < getLength(collection)) {
            throw new JXPathException("adjustment of " + collection + " to size " + size + " is not an expansion");
        }
        if (collection.getClass().isArray()) {
            Object bigger = Array.newInstance(collection.getClass().getComponentType(), size);
            System.arraycopy(collection, 0, bigger, 0, Array.getLength(collection));
            return bigger;
        }
        if (collection instanceof Collection) {
            while (((Collection) collection).size() < size) {
                ((Collection) collection).add(null);
            }
            return collection;
        }
        throw new JXPathException("Cannot turn " + collection.getClass().getName() + " into a collection of size " + size);
    }
