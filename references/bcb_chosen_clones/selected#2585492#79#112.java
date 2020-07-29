    public static final Object getObject(final ApplicationContext applicationContext, final Bean bean, final Construct construct) throws Exception {
        final Class<?> clazz = bean.getType();
        Object object = null;
        Object[] args = null;
        Integer constructArgsLength = 0;
        if ((construct != null) && (construct.getArguments() != null)) {
            args = construct.getArguments().toArray();
            constructArgsLength = args.length;
        }
        for (final Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length == constructArgsLength) {
                object = constructor.newInstance(args == null ? new Object[0] : args);
                if (bean.getStaticField() != null) {
                    object = object.getClass().getField(bean.getStaticField()).get(object);
                }
                break;
            }
        }
        if ((object == null) && (bean.getFactoryMethod() != null)) {
            object = bean.getType().getMethod(bean.getFactoryMethod()).invoke(new Object[0]);
        }
        if (object != null) {
            bean.setCreated(true);
            if (object instanceof FactoryBean<?>) {
                final FactoryBean<?> factoryBean = (FactoryBean<?>) object;
                applicationContext.addObject(bean.getId() == null ? null : AMPERSAND.concat(bean.getId()), factoryBean);
                applicationContext.addObject(bean.getId(), factoryBean.getObject());
            } else {
                applicationContext.addObject(bean.getId(), object);
            }
            return object;
        }
        throw new ApplicationContextException("Could not instantiate: " + bean);
    }
