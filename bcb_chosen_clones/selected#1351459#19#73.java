    public static GenericHelper getHelper(String helperName) {
        GenericHelper helper = (GenericHelper) helperCache.get(helperName);
        if (helper == null) {
            synchronized (GenericHelperFactory.class) {
                helper = (GenericHelper) helperCache.get(helperName);
                if (helper == null) {
                    try {
                        DatasourceInfo datasourceInfo = EntityConfigUtil.getDatasourceInfo(helperName);
                        if (datasourceInfo == null) {
                            throw new IllegalStateException("Could not find datasource definition with name " + helperName);
                        }
                        String helperClassName = datasourceInfo.helperClass;
                        Class helperClass = null;
                        if (helperClassName != null && helperClassName.length() > 0) {
                            try {
                                ClassLoader loader = Thread.currentThread().getContextClassLoader();
                                helperClass = loader.loadClass(helperClassName);
                            } catch (ClassNotFoundException e) {
                                Debug.logWarning(e, module);
                                throw new IllegalStateException("Error loading GenericHelper class \"" + helperClassName + "\": " + e.getMessage());
                            }
                        }
                        Class[] paramTypes = new Class[] { String.class };
                        Object[] params = new Object[] { helperName };
                        java.lang.reflect.Constructor helperConstructor = null;
                        if (helperClass != null) {
                            try {
                                helperConstructor = helperClass.getConstructor(paramTypes);
                            } catch (NoSuchMethodException e) {
                                Debug.logWarning(e, module);
                                throw new IllegalStateException("Error loading GenericHelper class \"" + helperClassName + "\": " + e.getMessage());
                            }
                        }
                        try {
                            helper = (GenericHelper) helperConstructor.newInstance(params);
                        } catch (IllegalAccessException e) {
                            Debug.logWarning(e, module);
                            throw new IllegalStateException("Error loading GenericHelper class \"" + helperClassName + "\": " + e.getMessage());
                        } catch (InstantiationException e) {
                            Debug.logWarning(e, module);
                            throw new IllegalStateException("Error loading GenericHelper class \"" + helperClassName + "\": " + e.getMessage());
                        } catch (java.lang.reflect.InvocationTargetException e) {
                            Debug.logWarning(e, module);
                            throw new IllegalStateException("Error loading GenericHelper class \"" + helperClassName + "\": " + e.getMessage());
                        }
                        if (helper != null) helperCache.put(helperName, helper);
                    } catch (SecurityException e) {
                        Debug.logError(e, module);
                        throw new IllegalStateException("Error loading GenericHelper class: " + e.toString());
                    }
                }
            }
        }
        return helper;
    }
