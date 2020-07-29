    public static Object remove(Object collection, int index) {
        collection = getValue(collection);
        if (collection == null) {
            return null;
        }
        if (index >= getLength(collection)) {
            throw new JXPathException("No such element at index " + index);
        }
        if (collection.getClass().isArray()) {
            int length = Array.getLength(collection);
            Object smaller = Array.newInstance(collection.getClass().getComponentType(), length - 1);
            if (index > 0) {
                System.arraycopy(collection, 0, smaller, 0, index);
            }
            if (index < length - 1) {
                System.arraycopy(collection, index + 1, smaller, index, length - index - 1);
            }
            return smaller;
        }
        if (collection instanceof List) {
            int size = ((List) collection).size();
            if (index < size) {
                ((List) collection).remove(index);
            }
            return collection;
        }
        if (collection instanceof Collection) {
            Iterator it = ((Collection) collection).iterator();
            for (int i = 0; i < index; i++) {
                if (!it.hasNext()) {
                    break;
                }
                it.next();
            }
            if (it.hasNext()) {
                it.next();
                it.remove();
            }
            return collection;
        }
        throw new JXPathException("Cannot remove " + collection.getClass().getName() + "[" + index + "]");
    }
