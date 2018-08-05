    @SuppressWarnings("unchecked")
    public static <T> T createGetSetBean(final Object o, Class<T> iType) {
        InvocationHandler handler = new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String methodName = method.getName();
                if (methodName.startsWith("get") || methodName.startsWith("is")) {
                    String pName = methodName.substring(3);
                    if (pName.length() >= 1) {
                        Object pValue = o.getClass().getField(pName).get(o);
                        if (pValue instanceof IReadProperty<?>) {
                            return ((IReadProperty<?>) pValue).get();
                        }
                        return pValue;
                    }
                } else if (methodName.startsWith("set")) {
                    String pName = methodName.substring(3);
                    if (pName.length() >= 1) {
                        Field pField = o.getClass().getField(pName);
                        if (IWriteProperty.class.isAssignableFrom(pField.getType())) {
                            ((IWriteProperty<Object>) pField.get(o)).set(args[0]);
                        } else {
                            pField.set(o, args[0]);
                        }
                        return null;
                    }
                }
                return o.getClass().getMethod(method.getName(), method.getParameterTypes()).invoke(o, args);
            }
        };
        Class<?> proxyClass = Proxy.getProxyClass(o.getClass().getClassLoader(), iType);
        try {
            final T instance = (T) proxyClass.getConstructor(new Class[] { InvocationHandler.class }).newInstance(new Object[] { handler });
            return instance;
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
    }
