    @SuppressWarnings("unchecked")
    public Object get(final Object bean, final Integer index, boolean createIfNeeded, boolean next) {
        try {
            Object value = this.get.invoke(bean);
            if (value == null && createIfNeeded) {
                value = create(this.type, this.componentType, index);
                if (value != null) {
                    this.set.invoke(bean, value);
                }
            }
            if (value != null && index != null) {
                if (value instanceof List) {
                    List<Object> list = (List<Object>) value;
                    final int size = list.size();
                    if (size > index) {
                        value = list.get(index);
                    } else {
                        value = null;
                        if (createIfNeeded) {
                            for (int i = size; i < index + 1; i++) {
                                list.add(null);
                            }
                        }
                    }
                    if (value == null && createIfNeeded) {
                        if (next) {
                            value = createComponent();
                            list.set(index, value);
                        } else {
                            value = list;
                        }
                    }
                } else if (value.getClass().isArray()) {
                    final int length = Array.getLength(value);
                    Object array = value;
                    value = length > index ? Array.get(value, index) : null;
                    if (value == null && createIfNeeded) {
                        if (length <= index) {
                            Object newArray = Array.newInstance(array.getClass().getComponentType(), index + 1);
                            System.arraycopy(array, 0, newArray, 0, length);
                            array = newArray;
                            this.set.invoke(bean, array);
                        }
                        if (next) {
                            value = createComponent();
                            Array.set(array, index, value);
                        }
                    }
                }
            }
            return value;
        } catch (IllegalArgumentException e) {
            throw new BexlException(e);
        } catch (IllegalAccessException e) {
            throw new BexlException(e);
        } catch (InvocationTargetException e) {
            throw new BexlException(e);
        } catch (SecurityException e) {
            throw new BexlException(e);
        }
    }
