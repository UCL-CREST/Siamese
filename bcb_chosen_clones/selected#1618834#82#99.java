    private DefaultPropertyEditor createInstance(Class<? extends DefaultPropertyEditor> clazz, Class<?> editClass) {
        for (Constructor con : clazz.getConstructors()) {
            if (con.getParameterTypes().length == 1 && con.getParameterTypes()[0] == Class.class) {
                try {
                    DefaultPropertyEditor back = (DefaultPropertyEditor) con.newInstance(editClass.getClasses());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        DefaultPropertyEditor back = null;
        try {
            clazz.newInstance();
        } catch (Exception e) {
            throw new ReflectionException(e, "Der PropertyEitor kann nciht erstellt werden");
        }
        return back;
    }
