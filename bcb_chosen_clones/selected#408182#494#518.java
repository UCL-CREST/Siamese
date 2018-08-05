    private Object createObject(String className, String value) {
        Object obj = null;
        try {
            Class parametro = Class.forName(className);
            Class[] paramTypes = { String.class };
            Constructor cons = parametro.getConstructor(paramTypes);
            Object[] args = { value };
            obj = cons.newInstance(args);
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
