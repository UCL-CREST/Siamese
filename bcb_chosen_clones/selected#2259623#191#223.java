    public void setPropertyValue(String propertyName, Object value) throws BeansException {
        try {
            if (isAllowedXBinding(propertyName)) {
                boolean createFirst = (jxpathcontext.getFactory() != null);
                if (createFirst) {
                    jxpathcontext.createPath(propertyName);
                }
                Pointer pointer = jxpathcontext.getPointer(propertyName);
                Object property = pointer.getValue();
                if (value.getClass().isArray()) {
                    Object[] values = (Object[]) value;
                    if (property != null && property.getClass().isArray()) {
                        Class componentType = property.getClass().getComponentType();
                        property = java.lang.reflect.Array.newInstance(componentType, values.length);
                        java.lang.System.arraycopy(values, 0, property, 0, values.length);
                        pointer.setValue(property);
                    } else if (property instanceof Collection) {
                        Collection cl = (Collection) property;
                        cl.clear();
                        cl.addAll(java.util.Arrays.asList(values));
                    }
                } else {
                    if (TypeUtils.canConvert(value, property.getClass())) {
                        pointer.setValue(value);
                    } else {
                        throw new TypeMismatchException(createPropertyChangeEvent(propertyName, property, value), property.getClass());
                    }
                }
            }
        } catch (JXPathException e) {
            throw new NotWritablePropertyException(getWrappedClass(), propertyName, e.getMessage(), e);
        }
    }
