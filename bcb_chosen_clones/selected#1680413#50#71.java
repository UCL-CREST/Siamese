    protected ViewComponentMetaDataStrategy determineStrategyFromClass(Class<?> type) {
        Class<?> origType = type;
        do {
            ComponentStrategy strategyAnno = type.getAnnotation(ComponentStrategy.class);
            if (strategyAnno != null) {
                Class<?> strategyClass = strategyAnno.strategy();
                try {
                    Constructor<?> ctor = strategyClass.getConstructor(Class.class);
                    ViewComponentMetaDataStrategy strategy = (ViewComponentMetaDataStrategy) ctor.newInstance(new Object[] { origType });
                    return strategy;
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e.getCause());
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(String.format("All classes that implement %s must have a one argument constructor that accepts a %s. This does not seem to be the case for %s", ViewComponentMetaDataStrategy.class.getName(), Class.class.getName(), strategyClass.getName()), e);
                } catch (Exception e) {
                    throw new RuntimeException("error while instanciating strategy object", e);
                }
            }
            type = type.getSuperclass();
        } while (!type.equals(ViewComponent.class));
        throw new RuntimeException(String.format("It seems that the component %s or any of it's parent classes does not have a metadata strategy attached.", origType.getName()));
    }
