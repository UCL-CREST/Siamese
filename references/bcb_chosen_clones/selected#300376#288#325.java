    protected Object instantiateModuleInstance() {
        Class moduleClass = moduleDef.getBuilderClass();
        Constructor[] constructors = moduleClass.getConstructors();
        if (constructors.length == 0) throw new RuntimeException(ProxyIOCMessages.noPublicConstructors(moduleClass));
        if (constructors.length > 1) {
            Comparator<Constructor> comparator = new Comparator<Constructor>() {

                public int compare(Constructor c1, Constructor c2) {
                    return c2.getParameterTypes().length - c1.getParameterTypes().length;
                }
            };
            Arrays.sort(constructors, comparator);
            logger.warn(ProxyIOCMessages.tooManyPublicConstructors(moduleClass, constructors[0]));
        }
        Constructor constructor = constructors[0];
        if (insideConstructor) throw new RuntimeException(ProxyIOCMessages.recursiveModuleConstructor(moduleClass, constructor));
        ObjectLocator locator = new ObjectLocatorImpl(registry, this);
        Map<Class, Object> resourcesMap = CollectionFactory.newMap();
        resourcesMap.put(Logger.class, logger);
        resourcesMap.put(ObjectLocator.class, locator);
        resourcesMap.put(OperationTracker.class, registry);
        InjectionResources resources = new MapInjectionResources(resourcesMap);
        Throwable fail = null;
        try {
            insideConstructor = true;
            Object[] parameterValues = InternalUtils.calculateParameters(locator, resources, constructor.getParameterTypes(), constructor.getGenericParameterTypes(), constructor.getParameterAnnotations(), registry);
            Object result = constructor.newInstance(parameterValues);
            InternalUtils.injectIntoFields(result, locator, resources, registry);
            return result;
        } catch (InvocationTargetException ex) {
            fail = ex.getTargetException();
        } catch (Exception ex) {
            fail = ex;
        } finally {
            insideConstructor = false;
        }
        throw new RuntimeException(ProxyIOCMessages.instantiateBuilderError(moduleClass, fail), fail);
    }
