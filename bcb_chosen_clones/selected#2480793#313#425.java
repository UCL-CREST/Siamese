    private static List lookupForImplementations(final Class clazz, final ClassLoader loader, final String[] defaultImplementations, final boolean onlyFirst, final boolean returnInstances) throws ClassNotFoundException {
        if (clazz == null) {
            throw new IllegalArgumentException("Argument 'clazz' cannot be null!");
        }
        ClassLoader classLoader = loader;
        if (classLoader == null) {
            classLoader = clazz.getClassLoader();
        }
        String interfaceName = clazz.getName();
        ArrayList tmp = new ArrayList();
        ArrayList toRemove = new ArrayList();
        String className = System.getProperty(interfaceName);
        if (className != null && className.trim().length() > 0) {
            tmp.add(className.trim());
        }
        Enumeration en = null;
        try {
            en = classLoader.getResources("META-INF/services/" + clazz.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (en != null && en.hasMoreElements()) {
            URL url = (URL) en.nextElement();
            InputStream is = null;
            try {
                is = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String line;
                do {
                    line = reader.readLine();
                    boolean remove = false;
                    if (line != null) {
                        if (line.startsWith("#-")) {
                            remove = true;
                            line = line.substring(2);
                        }
                        int pos = line.indexOf('#');
                        if (pos >= 0) {
                            line = line.substring(0, pos);
                        }
                        line = line.trim();
                        if (line.length() > 0) {
                            if (remove) {
                                toRemove.add(line);
                            } else {
                                tmp.add(line);
                            }
                        }
                    }
                } while (line != null);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (defaultImplementations != null) {
            for (int i = 0; i < defaultImplementations.length; i++) {
                tmp.add(defaultImplementations[i].trim());
            }
        }
        if (!clazz.isInterface()) {
            int m = clazz.getModifiers();
            if (!Modifier.isAbstract(m) && Modifier.isPublic(m) && !Modifier.isStatic(m)) {
                tmp.add(interfaceName);
            }
        }
        tmp.removeAll(toRemove);
        ArrayList res = new ArrayList();
        for (Iterator it = tmp.iterator(); it.hasNext(); ) {
            className = (String) it.next();
            try {
                Class c = Class.forName(className, false, classLoader);
                if (c != null) {
                    if (clazz.isAssignableFrom(c)) {
                        if (returnInstances) {
                            Object o = null;
                            try {
                                o = c.newInstance();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            if (o != null) {
                                res.add(o);
                                if (onlyFirst) {
                                    return res;
                                }
                            }
                        } else {
                            res.add(c);
                            if (onlyFirst) {
                                return res;
                            }
                        }
                    } else {
                        logger.warning("MetaInfLookup: Class '" + className + "' is not a subclass of class : " + interfaceName);
                    }
                }
            } catch (ClassNotFoundException e) {
                logger.log(Level.WARNING, "Cannot create implementation of interface: " + interfaceName, e);
            }
        }
        if (res.size() == 0) {
            throw new ClassNotFoundException("Cannot find any implemnetation of class " + interfaceName);
        }
        return res;
    }
