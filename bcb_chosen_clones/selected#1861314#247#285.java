    public static Adaptor getAdaptor(String kindTerm, Adaptable adaptable) throws AdaptorException {
        Class<Adaptor> adaptorClass = getAdaptorClass(kindTerm, adaptable);
        if (adaptorClass == null) {
            return null;
        }
        Adaptor adaptor = adaptable.getAdaptor(adaptorClass);
        if (adaptor == null) {
            Constructor<?> adaptorConstructor = null;
            Class<?> constructorArgClass = adaptable.getClass();
            while (constructorArgClass != null) {
                try {
                    adaptorConstructor = adaptorClass.getConstructor(constructorArgClass);
                    break;
                } catch (NoSuchMethodException nsme) {
                    constructorArgClass = constructorArgClass.getSuperclass();
                }
            }
            if (adaptorConstructor == null) {
                try {
                    adaptorConstructor = adaptorClass.getConstructor();
                } catch (NoSuchMethodException nsme) {
                    throw new AdaptorException("Unable to construct Adaptor " + adaptorClass + " instance for " + adaptable.getClass());
                }
            }
            try {
                if (constructorArgClass == null) {
                    adaptor = (Adaptor) adaptorConstructor.newInstance();
                } else {
                    adaptor = (Adaptor) adaptorConstructor.newInstance(adaptable);
                }
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e) {
                throw new AdaptorException("Unable to create kind Adaptor", e);
            }
            adaptable.addAdaptor(adaptor);
        }
        return adaptor;
    }
