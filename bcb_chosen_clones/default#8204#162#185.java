    private boolean mouseSecure() throws Exception {
        if (!mouseSecurity) {
            return true;
        }
        Class mouseInfoClass;
        Class pointerInfoClass;
        try {
            mouseInfoClass = Class.forName("java.awt.MouseInfo");
            pointerInfoClass = Class.forName("java.awt.PointerInfo");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return true;
        }
        Method getPointerInfo = mouseInfoClass.getMethod("getPointerInfo", new Class[0]);
        Method getLocation = pointerInfoClass.getMethod("getLocation", new Class[0]);
        Object pointer = null;
        try {
            pointer = getPointerInfo.invoke(pointerInfoClass, new Object[0]);
        } catch (java.lang.reflect.InvocationTargetException e) {
            e.getTargetException().printStackTrace();
        }
        Point mousePosition = (Point) (getLocation.invoke(pointer, new Object[0]));
        return mousePosition.x >= docScreenX && mousePosition.x <= docScreenXMax && mousePosition.y >= docScreenY && mousePosition.y <= docScreenYMax;
    }
