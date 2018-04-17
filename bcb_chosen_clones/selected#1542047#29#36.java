    static Object[] trim(Object[] elements, int len) {
        if (len == elements.length) {
            return elements;
        }
        Object[] nelements = (Object[]) Array.newInstance(elements.getClass().getComponentType(), len);
        System.arraycopy(elements, 0, nelements, 0, len);
        return nelements;
    }
