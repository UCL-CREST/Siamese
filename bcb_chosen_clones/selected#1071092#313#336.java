    private SceneGraphObject createNodeFromSuper(String className, Class[] parameterTypes, Object[] parameters) {
        SceneGraphObject ret;
        String tmp = this.getClass().getName();
        String superClass = tmp.substring(tmp.indexOf("state") + 6, tmp.length() - 5);
        Constructor constructor;
        try {
            Class state = Class.forName(superClass);
            constructor = state.getConstructor(parameterTypes);
            ret = (SceneGraphObject) constructor.newInstance(parameters);
        } catch (ClassNotFoundException e1) {
            throw new SGIORuntimeException("No State class for " + superClass);
        } catch (IllegalAccessException e2) {
            throw new SGIORuntimeException("Broken State class for " + className + " - IllegalAccess");
        } catch (InstantiationException e3) {
            throw new SGIORuntimeException("Broken State class for " + className);
        } catch (java.lang.reflect.InvocationTargetException e4) {
            throw new SGIORuntimeException("InvocationTargetException for " + className);
        } catch (NoSuchMethodException e5) {
            for (int i = 0; i < parameterTypes.length; i++) System.err.println(parameterTypes[i].getName());
            System.err.println("------");
            throw new SGIORuntimeException("Invalid constructor for " + className);
        }
        return ret;
    }
