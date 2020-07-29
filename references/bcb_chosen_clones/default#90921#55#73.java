    static Object createProxy(Object proxyMe) {
        InvocationHandler handler = new MyInvocationHandler(proxyMe);
        Class proxyClass = Proxy.getProxyClass(Shapes.class.getClassLoader(), new Class[] { Quads.class, Colors.class });
        Object proxy = null;
        try {
            Constructor<Class> cons;
            cons = proxyClass.getConstructor(new Class[] { InvocationHandler.class });
            proxy = cons.newInstance(new Object[] { handler });
        } catch (NoSuchMethodException nsme) {
            System.err.println("failed: " + nsme);
        } catch (InstantiationException ie) {
            System.err.println("failed: " + ie);
        } catch (IllegalAccessException ie) {
            System.err.println("failed: " + ie);
        } catch (InvocationTargetException ite) {
            System.err.println("failed: " + ite);
        }
        return proxy;
    }
