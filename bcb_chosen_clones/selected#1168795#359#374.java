    private Exception readExceptionValue(TypeDescription type) {
        Exception value;
        try {
            value = (Exception) type.getZClass().getConstructor(new Class[] { String.class }).newInstance(new Object[] { readStringValue() });
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.toString());
        } catch (InstantiationException e) {
            throw new RuntimeException(e.toString());
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.toString());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.toString());
        }
        readFields(type, value);
        return value;
    }
