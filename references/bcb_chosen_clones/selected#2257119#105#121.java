    public Object newProxyInstance(Class proxyClass) throws IllegalArgumentException {
        if (!Proxy.isProxyClass(proxyClass)) {
            throw new IllegalArgumentException("This class is not a proxy.");
        }
        try {
            Constructor cons = proxyClass.getConstructor(constructorParams);
            return cons.newInstance(new Object[] { new Jdk13InvocationHandler() });
        } catch (NoSuchMethodException e) {
            throw new InternalError(e.toString());
        } catch (IllegalAccessException e) {
            throw new InternalError(e.toString());
        } catch (InstantiationException e) {
            throw new InternalError(e.toString());
        } catch (InvocationTargetException e) {
            throw new InternalError(e.toString());
        }
    }
