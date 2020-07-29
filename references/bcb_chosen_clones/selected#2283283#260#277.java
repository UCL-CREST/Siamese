    private Object validateArray(final Class propertyType, final Object o, final int size) throws BeanException {
        if (propertyType.isArray() == false) {
            throw new BeanException("The property's value is no array.");
        }
        if (o == null) {
            return Array.newInstance(propertyType.getComponentType(), size + 1);
        }
        if (o.getClass().isArray() == false) {
            throw new BeanException("The property's value is no array.");
        }
        final int length = Array.getLength(o);
        if (length > size) {
            return o;
        }
        final Object retval = Array.newInstance(o.getClass().getComponentType(), size + 1);
        System.arraycopy(o, 0, retval, 0, length);
        return o;
    }
