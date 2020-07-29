    private Object createInstance(Object parentInstance, MOConstructorFacadeParamValues providedArgValues, MOBeanDescriptor concreteBeanDescriptor) throws MOBeanInstansationException {
        try {
            MOConstructorArg[] constructorArgs = getConstructorArgs();
            Class<?>[] argTypes = new Class<?>[constructorArgs.length];
            Object[] argValues = new Object[constructorArgs.length];
            for (int i = 0; i < constructorArgs.length; i++) {
                if (constructorArgs[i].sourcePropertyOf() == SourcePropertyOf.parent) {
                    argTypes[i] = getArgType(parentBeanDescriptor, constructorArgs[i].sourceProperty());
                    argValues[i] = getParentArgValue(parentInstance, constructorArgs[i].sourceProperty());
                } else {
                    argTypes[i] = getArgType(beanDescriptor, constructorArgs[i].sourceProperty());
                    argValues[i] = providedArgValues.getValue(constructorArgs[i].sourceProperty());
                }
            }
            if (constructionDescriptor == null) {
                return concreteBeanDescriptor.getBeanClass().newInstance();
            } else if (!hasFactoryMethod()) {
                return concreteBeanDescriptor.getBeanClass().getConstructor(argTypes).newInstance(argValues);
            } else if (hasFactoryClass()) {
                return constructionDescriptor.factoryClass().getMethod(constructionDescriptor.factoryMethod(), argTypes).invoke(null, argValues);
            } else {
                return parentInstance.getClass().getMethod(constructionDescriptor.factoryMethod(), argTypes).invoke(parentInstance, argValues);
            }
        } catch (InstantiationException e) {
            throw new MOBeanInstansationException(e, concreteBeanDescriptor.getBeanClass());
        } catch (IllegalAccessException e) {
            throw new MOBeanInstansationException(e, concreteBeanDescriptor.getBeanClass());
        } catch (InvocationTargetException e) {
            throw new MOBeanInstansationException(e, concreteBeanDescriptor.getBeanClass());
        } catch (NoSuchMethodException e) {
            throw new MOBeanInstansationException(e, concreteBeanDescriptor.getBeanClass());
        } catch (MOBeansException e) {
            throw new MOBeanInstansationException(e, concreteBeanDescriptor.getBeanClass());
        }
    }
