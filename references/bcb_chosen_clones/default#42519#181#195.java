    protected Point getDesktopMousePosition() throws Exception {
        Class mouseInfoClass;
        Class pointerInfoClass;
        mouseInfoClass = Class.forName("java.awt.MouseInfo");
        pointerInfoClass = Class.forName("java.awt.PointerInfo");
        Method getPointerInfo = mouseInfoClass.getMethod("getPointerInfo", new Class[0]);
        Method getLocation = pointerInfoClass.getMethod("getLocation", new Class[0]);
        Object pointer = null;
        try {
            pointer = getPointerInfo.invoke(pointerInfoClass, new Object[0]);
        } catch (java.lang.reflect.InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }
        return (Point) (getLocation.invoke(pointer, new Object[0]));
    }
