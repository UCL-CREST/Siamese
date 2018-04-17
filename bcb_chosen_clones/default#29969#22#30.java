    private void disableFocusTraversalKeys() {
        try {
            Class[] argClasses = { Boolean.TYPE };
            java.lang.reflect.Method method = getClass().getMethod("setFocusTraversalKeysEnabled", argClasses);
            Object[] argObjects = { new Boolean(false) };
            method.invoke(this, argObjects);
        } catch (Exception e) {
        }
    }
