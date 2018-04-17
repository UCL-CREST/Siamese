    @SuppressWarnings({ "unchecked" })
    private Object getField(AbstractLine line, JRField field, String fieldName) {
        Object ret = "";
        try {
            AbstractFieldValue retVal = line.getFieldValue(fieldName);
            ret = null;
            if (field.getValueClass() == String.class) {
                ret = retVal.asString();
            } else if (field.getValueClass() == Long.class) {
                ret = retVal.asLong();
            } else if (field.getValueClass() == BigDecimal.class) {
                ret = retVal.asBigDecimal();
            } else if (field.getValueClass() == Double.class) {
                ret = retVal.asDouble();
            } else {
                Object[] params = { retVal.asString() };
                Class[] c = { String.class };
                ret = field.getValueClass().getConstructor(c).newInstance(params);
            }
        } catch (Exception e) {
            System.out.println("*** Error in Field: " + fieldName + " >> " + e.getLocalizedMessage());
        }
        return ret;
    }
