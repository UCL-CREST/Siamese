    protected SceneGraphObject createNode(Class Ding3dClass, Class[] parameterTypes, Object[] parameters) {
        SceneGraphObject ret;
        Constructor constructor;
        try {
            constructor = Ding3dClass.getConstructor(parameterTypes);
            ret = (SceneGraphObject) constructor.newInstance(parameters);
        } catch (IllegalAccessException e2) {
            throw new SGIORuntimeException("Broken State class for " + Ding3dClass.getClass().getName() + " - IllegalAccess");
        } catch (InstantiationException e3) {
            throw new SGIORuntimeException("Broken State class for " + Ding3dClass.getClass().getName());
        } catch (java.lang.reflect.InvocationTargetException e4) {
            throw new SGIORuntimeException("InvocationTargetException for " + Ding3dClass.getClass().getName());
        } catch (NoSuchMethodException e5) {
            for (int i = 0; i < parameterTypes.length; i++) System.err.println(parameterTypes[i].getName());
            System.err.println("------");
            throw new SGIORuntimeException("Invalid constructor for " + Ding3dClass.getClass().getName());
        }
        return ret;
    }
