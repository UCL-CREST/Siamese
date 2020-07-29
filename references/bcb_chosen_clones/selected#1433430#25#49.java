    public Seasar2ApplicationContext(ServletContext context) throws ApplicationContextException {
        try {
            Class<?> cls = Class.forName("org.seasar.framework.container.servlet.SingletonS2ContainerInitializer");
            Object obj = cls.getConstructor(new Class[0]).newInstance(new Object[0]);
            if (context.getInitParameter("configPath") != null) {
                MethodUtils.invokeMethod(obj, "setConfigPath", new Object[] { context.getInitParameter("configPath") });
            }
            MethodUtils.invokeMethod(obj, "setApplication", new Object[] { context });
            MethodUtils.invokeMethod(obj, "initialize", new Object[0]);
        } catch (IllegalArgumentException e) {
            throw new ApplicationContextException(e);
        } catch (SecurityException e) {
            throw new ApplicationContextException(e);
        } catch (InstantiationException e) {
            throw new ApplicationContextException(e);
        } catch (ClassNotFoundException e) {
            throw new ApplicationContextException(e);
        } catch (NoSuchMethodException e) {
            throw new ApplicationContextException(e);
        } catch (IllegalAccessException e) {
            throw new ApplicationContextException(e);
        } catch (InvocationTargetException e) {
            throw new ApplicationContextException(e);
        }
    }
