    @SuppressWarnings("unchecked")
    private static Object bindingField(Object parent, Object value, Class<?> componentType, String name, Object param) {
        ReflectField field = null;
        Object top = null != value ? value : parent;
        OGNode[] nodes = parse(name);
        if (null != nodes) {
            boolean match = true;
            int len = nodes.length;
            for (int i = 0; i < len; i++) {
                OGNode node = nodes[i];
                if (null != parent) {
                    ReflectType parentType = ReflectType.get(parent.getClass());
                    field = parentType.findField(node.prop);
                    if (null != field) {
                        if (!node.isArray() && i == len - 1) {
                            try {
                                field.setValue(parent, Converter.convert(field.getType(), field.getGenericType(), param));
                                break;
                            } catch (Exception e) {
                                throw new BindingException("binding field '" + parentType.getActualType().getName() + "." + field.getName() + "' error", e);
                            }
                        } else {
                            value = field.getValue(parent);
                            if (null == value) {
                                value = Converter.defaultInstance(field.getType());
                                if (null != value) {
                                    field.setValue(parent, value);
                                }
                            }
                        }
                    } else {
                        match = false;
                    }
                }
                if (match) {
                    if (node.index != null) {
                        if (value instanceof List) {
                            parent = value;
                            Class<?> elementType = null != field ? ReflectUtils.getActualType(field.getGenericType()) : componentType;
                            List list = (List) value;
                            if (node.index > list.size()) {
                                for (int j = list.size(); j < node.index; j++) {
                                    list.add(Converter.defaultValue(elementType));
                                }
                            }
                            if (i == len - 1) {
                                if (node.index >= list.size()) {
                                    list.add(Converter.convert(elementType, param));
                                } else {
                                    list.set(node.index, Converter.convert(elementType, param));
                                }
                            } else {
                                Object itemValue = node.index < list.size() ? list.get(node.index) : null;
                                if (null == itemValue) {
                                    itemValue = Converter.defaultInstance(elementType);
                                    if (node.index >= list.size()) {
                                        list.add(itemValue);
                                    } else {
                                        list.set(node.index, itemValue);
                                    }
                                }
                                parent = itemValue;
                            }
                        } else if (value.getClass().isArray()) {
                            componentType = value.getClass().getComponentType();
                            int arrayLength = Array.getLength(value);
                            if (node.index >= arrayLength) {
                                Object newArray = Array.newInstance(componentType, node.index + 1);
                                if (arrayLength > 0) {
                                    System.arraycopy(value, 0, newArray, 0, arrayLength);
                                }
                                value = newArray;
                                if (null != field) {
                                    field.setValue(parent, value);
                                } else if (i == 0) {
                                    top = value;
                                }
                                arrayLength = node.index + 1;
                            }
                            parent = value;
                            if (i == len - 1) {
                                Array.set(value, node.index, Converter.convert(componentType, param));
                            } else {
                                Object itemValue = node.index < arrayLength ? Array.get(value, node.index) : null;
                                if (null == itemValue) {
                                    itemValue = Converter.defaultInstance(componentType);
                                    Array.set(value, node.index, itemValue);
                                }
                                parent = itemValue;
                            }
                        }
                    } else if (node.name != null) {
                        parent = value;
                        if (value instanceof Map) {
                            Type[] genericTypes = ReflectUtils.getActualTypes(field.getGenericType());
                            Class<?> keyType = ReflectUtils.getActualType(genericTypes[0]);
                            Class<?> valueType = ReflectUtils.getActualType(genericTypes[1]);
                            Map propMap = (Map) value;
                            Object keyValue = Converter.convert(keyType, node.name);
                            if (i == len - 1) {
                                propMap.put(keyValue, Converter.convert(valueType, param));
                            } else {
                                parent = propMap.get(keyValue);
                                if (null == parent) {
                                    parent = Converter.defaultInstance(valueType);
                                    propMap.put(Converter.convert(keyType, node.name), parent);
                                }
                            }
                        } else {
                            ReflectType propType = ReflectType.get(field.getType());
                            ReflectField propField = propType.findField(node.name);
                            if (i == len - 1) {
                                if (null != propField) {
                                    propField.setValue(value, Converter.convert(propField.getType(), propField.getGenericType(), param));
                                }
                            } else {
                                if (null == propField) {
                                    break;
                                } else {
                                    parent = Converter.convert(propField.getType(), propField.getGenericType(), param);
                                }
                            }
                        }
                    } else if (i == len - 2) {
                        parent = value;
                        OGNode last = nodes[++i];
                        field = ReflectType.get(parent.getClass()).findField(last.prop);
                        if (null != field) {
                            field.setValue(parent, Converter.convert(field.getType(), field.getGenericType(), param));
                        }
                    } else {
                        parent = value;
                    }
                } else {
                    break;
                }
            }
        }
        return top;
    }
