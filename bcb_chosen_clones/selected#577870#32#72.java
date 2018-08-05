                        public Constructor run() throws Exception {
                            String path = "META-INF/services/" + ComponentApplicationContext.class.getName();
                            ClassLoader loader = Thread.currentThread().getContextClassLoader();
                            final Enumeration<URL> urls;
                            if (loader == null) {
                                urls = ComponentApplicationContext.class.getClassLoader().getResources(path);
                            } else {
                                urls = loader.getResources(path);
                            }
                            while (urls.hasMoreElements()) {
                                URL url = urls.nextElement();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                                try {
                                    String className = null;
                                    while ((className = reader.readLine()) != null) {
                                        final String name = className.trim();
                                        if (!name.startsWith("#") && !name.startsWith(";") && !name.startsWith("//")) {
                                            final Class<?> cls;
                                            if (loader == null) {
                                                cls = Class.forName(name);
                                            } else {
                                                cls = Class.forName(name, true, loader);
                                            }
                                            int m = cls.getModifiers();
                                            if (ComponentApplicationContext.class.isAssignableFrom(cls) && !Modifier.isAbstract(m) && !Modifier.isInterface(m)) {
                                                Constructor constructor = cls.getDeclaredConstructor();
                                                if (!Modifier.isPublic(constructor.getModifiers())) {
                                                    constructor.setAccessible(true);
                                                }
                                                return constructor;
                                            } else {
                                                throw new ClassCastException(cls.getName());
                                            }
                                        }
                                    }
                                } finally {
                                    reader.close();
                                }
                            }
                            throw new ComponentApplicationException("No " + "ComponentApplicationContext implementation " + "found.");
                        }
