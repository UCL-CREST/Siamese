    public static String getSignatureAttribute(Object obj) {
        Method method;
        try {
            if (obj instanceof AccessibleObject) {
                method = AccessibleObject.class.getDeclaredMethod("getSignatureAttribute");
            } else {
                method = Class.class.getDeclaredMethod("getSignatureAttribute");
            }
            method.setAccessible(true);
        } catch (NoSuchMethodException ex) {
            System.err.println("getSignatureAttribute() not defined.");
            ex.printStackTrace();
            return "<unknown>";
        }
        try {
            return (String) method.invoke(obj);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
