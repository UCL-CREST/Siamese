    private Object initObject(Class c, String className, String type) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            errors.add(new GeneralError(type + " plugin " + className + " does not exist"));
            return null;
        }
        try {
            Class<?>[] args = new Class<?>[] { Session.class };
            Constructor constructor = c.getClassLoader().loadClass(className).getConstructor(args);
            return constructor.newInstance(new Object[] { s });
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
