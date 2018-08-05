    public void set(Object obj, int i, Object newValue) throws InvocationTargetException, IllegalAccessException {
        if (isIndexed()) {
            IndexedPropertyDescriptor id = (IndexedPropertyDescriptor) myPD;
            growArrayToSize(obj, id.getIndexedPropertyType(), i);
            id.getIndexedWriteMethod().invoke(obj, new Object[] { new Integer(i), newValue });
        } else {
            Object array = get(obj);
            if (array == null || Array.getLength(array) <= i) {
                Class componentType = getType().getComponentType();
                Object newArray = Array.newInstance(componentType, i + 1);
                if (array != null) {
                    System.arraycopy(array, 0, newArray, 0, Array.getLength(array));
                }
                array = newArray;
            }
            Array.set(array, i, newValue);
            set(obj, array);
        }
    }
