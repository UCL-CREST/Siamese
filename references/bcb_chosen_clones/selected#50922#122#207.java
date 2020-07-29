    private void loadProperty(Object bean, PropertyDescriptor propDescr, IXMLElement propElem) throws XMLException {
        final Method setter = propDescr.getWriteMethod();
        if (setter != null) {
            final Class parmType = setter.getParameterTypes()[0];
            final Class arrayType = parmType.getComponentType();
            final String value = propElem.getContent();
            if (isIndexedElement(propElem)) {
                Object[] data = loadIndexedProperty(bean, propDescr, propElem);
                try {
                    Object obj = Array.newInstance(arrayType, data.length);
                    System.arraycopy(data, 0, obj, 0, data.length);
                    setter.invoke(bean, new Object[] { obj });
                } catch (Exception ex) {
                    throw new XMLException(ex);
                }
            } else if (isBeanElement(propElem)) {
                Object data = loadBean(propElem);
                try {
                    setter.invoke(bean, new Object[] { data });
                } catch (Exception ex) {
                    throw new XMLException(ex);
                }
            } else if (parmType == boolean.class) {
                Object data = new Boolean(value);
                try {
                    setter.invoke(bean, new Object[] { data });
                } catch (Exception ex) {
                    throw new XMLException(ex);
                }
            } else if (parmType == int.class) {
                Object data = new Integer(value);
                try {
                    setter.invoke(bean, new Object[] { data });
                } catch (Exception ex) {
                    throw new XMLException(ex);
                }
            } else if (parmType == short.class) {
                Object data = new Short(value);
                try {
                    setter.invoke(bean, new Object[] { data });
                } catch (Exception ex) {
                    throw new XMLException(ex);
                }
            } else if (parmType == long.class) {
                Object data = new Long(value);
                try {
                    setter.invoke(bean, new Object[] { data });
                } catch (Exception ex) {
                    throw new XMLException(ex);
                }
            } else if (parmType == float.class) {
                Object data = new Float(value);
                try {
                    setter.invoke(bean, new Object[] { data });
                } catch (Exception ex) {
                    throw new XMLException(ex);
                }
            } else if (parmType == double.class) {
                Object data = new Double(value);
                try {
                    setter.invoke(bean, new Object[] { data });
                } catch (Exception ex) {
                    throw new XMLException(ex);
                }
            } else if (parmType == char.class) {
                Object data;
                if (value != null && value.length() > 0) {
                    data = new Character(value.charAt(0));
                } else {
                    data = new Character(' ');
                }
                try {
                    setter.invoke(bean, new Object[] { data });
                } catch (Exception ex) {
                    throw new XMLException(ex);
                }
            } else {
                Object data = value;
                try {
                    setter.invoke(bean, new Object[] { data });
                } catch (Exception ex) {
                    throw new XMLException(ex);
                }
            }
        }
    }
