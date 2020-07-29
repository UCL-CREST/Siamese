    protected Object getValueBody(OgnlContext context, Object source) throws OgnlException {
        Object result = source;
        for (int i = 0, ilast = _children.length - 1; i <= ilast; ++i) {
            boolean handled = false;
            if (i < ilast) {
                if (_children[i] instanceof ASTProperty) {
                    ASTProperty propertyNode = (ASTProperty) _children[i];
                    int indexType = propertyNode.getIndexedPropertyType(context, result);
                    if ((indexType != OgnlRuntime.INDEXED_PROPERTY_NONE) && (_children[i + 1] instanceof ASTProperty)) {
                        ASTProperty indexNode = (ASTProperty) _children[i + 1];
                        if (indexNode.isIndexedAccess()) {
                            Object index = indexNode.getProperty(context, result);
                            if (index instanceof DynamicSubscript) {
                                if (indexType == OgnlRuntime.INDEXED_PROPERTY_INT) {
                                    Object array = propertyNode.getValue(context, result);
                                    int len = Array.getLength(array);
                                    switch(((DynamicSubscript) index).getFlag()) {
                                        case DynamicSubscript.ALL:
                                            result = Array.newInstance(array.getClass().getComponentType(), len);
                                            System.arraycopy(array, 0, result, 0, len);
                                            handled = true;
                                            i++;
                                            break;
                                        case DynamicSubscript.FIRST:
                                            index = new Integer((len > 0) ? 0 : -1);
                                            break;
                                        case DynamicSubscript.MID:
                                            index = new Integer((len > 0) ? (len / 2) : -1);
                                            break;
                                        case DynamicSubscript.LAST:
                                            index = new Integer((len > 0) ? (len - 1) : -1);
                                            break;
                                    }
                                } else {
                                    if (indexType == OgnlRuntime.INDEXED_PROPERTY_OBJECT) {
                                        throw new OgnlException("DynamicSubscript '" + indexNode + "' not allowed for object indexed property '" + propertyNode + "'");
                                    }
                                }
                            }
                            if (!handled) {
                                result = OgnlRuntime.getIndexedProperty(context, result, propertyNode.getProperty(context, result).toString(), index);
                                handled = true;
                                i++;
                            }
                        }
                    }
                }
            }
            if (!handled) {
                result = _children[i].getValue(context, result);
            }
        }
        return result;
    }
