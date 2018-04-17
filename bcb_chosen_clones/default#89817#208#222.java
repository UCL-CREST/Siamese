    public String invokeStringGetter(Class cls, String methodname) {
        try {
            Method mid = cls.getMethod(methodname, null);
            String id = (String) mid.invoke(null, null);
            return id;
        } catch (NoSuchMethodException e) {
            return null;
        } catch (InvocationTargetException e) {
            reportException(cls, e.getCause());
            return null;
        } catch (Exception e) {
            reportException(cls, e);
            return null;
        }
    }
