    @SuppressWarnings("unchecked")
    private <T> T getApplicationObject(Class<T> interfaceClass, Class<? extends T> extendedInterfaceClass, Class<? extends T> extendedInterfaceWrapperClass, Collection<String> classNamesIterator, T defaultObject) {
        T current = defaultObject;
        for (String implClassName : classNamesIterator) {
            Class<? extends T> implClass = ClassUtils.simpleClassForName(implClassName);
            if (!interfaceClass.isAssignableFrom(implClass)) {
                throw new IllegalArgumentException("Class " + implClassName + " is no " + interfaceClass.getName());
            }
            if (current == null) {
                current = (T) ClassUtils.newInstance(implClass);
            } else {
                T newCurrent = null;
                try {
                    Constructor<? extends T> delegationConstructor = null;
                    if (extendedInterfaceClass != null && extendedInterfaceClass.isAssignableFrom(current.getClass())) {
                        try {
                            delegationConstructor = implClass.getConstructor(new Class[] { extendedInterfaceClass });
                        } catch (NoSuchMethodException mnfe) {
                        }
                    }
                    if (delegationConstructor == null) {
                        delegationConstructor = implClass.getConstructor(new Class[] { interfaceClass });
                    }
                    try {
                        newCurrent = delegationConstructor.newInstance(new Object[] { current });
                    } catch (InstantiationException e) {
                        log.log(Level.SEVERE, e.getMessage(), e);
                        throw new FacesException(e);
                    } catch (IllegalAccessException e) {
                        log.log(Level.SEVERE, e.getMessage(), e);
                        throw new FacesException(e);
                    } catch (InvocationTargetException e) {
                        log.log(Level.SEVERE, e.getMessage(), e);
                        throw new FacesException(e);
                    }
                } catch (NoSuchMethodException e) {
                    newCurrent = (T) ClassUtils.newInstance(implClass);
                }
                if (extendedInterfaceWrapperClass != null && !extendedInterfaceClass.isAssignableFrom(newCurrent.getClass())) {
                    try {
                        Constructor<? extends T> wrapperConstructor = extendedInterfaceWrapperClass.getConstructor(new Class[] { interfaceClass, extendedInterfaceClass });
                        newCurrent = wrapperConstructor.newInstance(new Object[] { newCurrent, current });
                    } catch (NoSuchMethodException e) {
                        log.log(Level.SEVERE, e.getMessage(), e);
                        throw new FacesException(e);
                    } catch (InstantiationException e) {
                        log.log(Level.SEVERE, e.getMessage(), e);
                        throw new FacesException(e);
                    } catch (IllegalAccessException e) {
                        log.log(Level.SEVERE, e.getMessage(), e);
                        throw new FacesException(e);
                    } catch (InvocationTargetException e) {
                        log.log(Level.SEVERE, e.getMessage(), e);
                        throw new FacesException(e);
                    }
                }
                current = newCurrent;
            }
        }
        return current;
    }
