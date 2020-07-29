    public static final void create(String classname, Object[] params) {
        assert classname != null;
        if (getInstance() == null) {
            synchronized (Locator.class) {
                if (getInstance() == null) {
                    try {
                        log.info("create locator with class " + classname);
                        Class<Locator> clazz = (Class<Locator>) Class.forName(classname);
                        Locator locator = null;
                        if (params == null) {
                            locator = clazz.newInstance();
                        } else {
                            Class[] types = new Class[params.length];
                            for (int i = 0; i < types.length; i++) {
                                types[i] = params[i].getClass();
                            }
                            Constructor<Locator> constructor = clazz.getConstructor(types);
                            locator = constructor.newInstance(params);
                        }
                        register(locator);
                    } catch (ClassNotFoundException e) {
                        log.fatal("Could not retrieve locator class " + classname, e);
                    } catch (InstantiationException e) {
                        log.fatal("Could not instatantiate locator class " + classname, e);
                    } catch (IllegalAccessException e) {
                        log.fatal("No access to locator class " + classname, e);
                    } catch (SecurityException e) {
                        log.fatal("No security access to locator class " + classname, e);
                    } catch (NoSuchMethodException e) {
                        log.fatal("No constructor with request signature in locator class " + classname, e);
                    } catch (IllegalArgumentException e) {
                        log.fatal("Wrong parameter list in locator class " + classname, e);
                    } catch (InvocationTargetException e) {
                        log.fatal("Target invocation exception in locator class " + classname, e);
                    }
                }
            }
        }
    }
