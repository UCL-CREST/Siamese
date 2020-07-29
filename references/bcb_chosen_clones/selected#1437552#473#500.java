    protected Object growIndexedProperty(String name, Object indexedProperty, int index) {
        if (indexedProperty instanceof List) {
            List list = (List) indexedProperty;
            while (index >= list.size()) {
                Class contentType = getDynaClass().getDynaProperty(name).getContentType();
                Object value = null;
                if (contentType != null) {
                    value = createProperty(name + "[" + list.size() + "]", contentType);
                }
                list.add(value);
            }
        }
        if ((indexedProperty.getClass().isArray())) {
            int length = Array.getLength(indexedProperty);
            if (index >= length) {
                Class componentType = indexedProperty.getClass().getComponentType();
                Object newArray = Array.newInstance(componentType, (index + 1));
                System.arraycopy(indexedProperty, 0, newArray, 0, length);
                indexedProperty = newArray;
                set(name, indexedProperty);
                int newLength = Array.getLength(indexedProperty);
                for (int i = length; i < newLength; i++) {
                    Array.set(indexedProperty, i, createProperty(name + "[" + i + "]", componentType));
                }
            }
        }
        return indexedProperty;
    }
