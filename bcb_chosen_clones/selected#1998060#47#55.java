    public Object createNewObject(Class aClass, Class<?>[] paramClasses, Object[] params) {
        try {
            Constructor c = aClass.getConstructor(paramClasses);
            Object obj = c.newInstance(params);
            return obj;
        } catch (Exception ex) {
            throw new TiiraException(ex);
        }
    }
