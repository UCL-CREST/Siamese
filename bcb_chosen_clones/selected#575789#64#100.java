    public void setProperty(Map context, Object target, Object name, Object value) throws OgnlException {
        Object index = name;
        boolean isNumber = (index instanceof Number);
        if (isNumber || (index instanceof DynamicSubscript)) {
            TypeConverter converter = ((OgnlContext) context).getTypeConverter();
            Object convertedValue;
            convertedValue = converter.convertValue(context, target, null, name.toString(), value, target.getClass().getComponentType());
            if (isNumber) {
                int i = ((Number) index).intValue();
                if (i >= 0) {
                    Array.set(target, i, convertedValue);
                }
            } else {
                int len = Array.getLength(target);
                switch(((DynamicSubscript) index).getFlag()) {
                    case DynamicSubscript.ALL:
                        System.arraycopy(target, 0, convertedValue, 0, len);
                        return;
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
            }
        } else {
            if (name instanceof String) {
                super.setProperty(context, target, name, value);
            } else {
                throw new NoSuchPropertyException(target, index);
            }
        }
    }
