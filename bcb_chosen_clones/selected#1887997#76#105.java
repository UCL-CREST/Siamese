    @SuppressWarnings("unchecked")
    public <T extends Provider> T newProxyInstance() {
        long num = getUniqueNumber();
        String proxyPkg = "";
        if (!Modifier.isPublic(providerType.getModifiers())) {
            String name = providerType.getName();
            int n = name.lastIndexOf('.');
            proxyPkg = ((n == -1) ? "" : name.substring(0, n + 1));
        }
        String proxyName = proxyPkg + proxyClassNamePrefix + num;
        Class<?> proxyClass = null;
        byte[] proxyClassFile = ProxyGenerator.generateProxyClass(proxyName, new Class<?>[] { providerType });
        try {
            proxyClass = JVM.defineClass(providerType.getClassLoader(), proxyName, proxyClassFile, 0, proxyClassFile.length);
        } catch (ClassFormatError e) {
            throw new IllegalArgumentException(e.toString());
        }
        try {
            Constructor cons = proxyClass.getConstructor(constructorParams);
            return (T) cons.newInstance(new Object[] { this });
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
