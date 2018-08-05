    public Object createInstance(Class context) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        Object result = null;
        Method instantiator = this.getSingletonInstantiator(context);
        if (null != instantiator) {
            result = instantiator.invoke(context);
        } else {
            for (Constructor c : context.getConstructors()) {
                if (Modifier.isPublic(c.getModifiers()) && c.getParameterTypes().length == 0) {
                    result = c.newInstance();
                }
            }
        }
        return result;
    }
