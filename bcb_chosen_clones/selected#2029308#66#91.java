            public void run() {
                try {
                    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                    Class<?> checkClassAdapter = classloader.loadClass("org.objectweb.asm.util.CheckClassAdapter");
                    Class<?> classReader = classloader.loadClass("org.objectweb.asm.ClassReader");
                    Constructor<?> constructor = classReader.getConstructor(InputStream.class);
                    Method verify = checkClassAdapter.getMethod("verify", classReader, Boolean.TYPE, PrintWriter.class);
                    Enumeration<JarEntry> entries = jarFile.entries();
                    Log log = getLog();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.endsWith(".class") && !name.startsWith("net/sourceforge/comeback/") && !name.startsWith("org/objectweb/asm/")) {
                            log.info("Verifying " + entry.getName());
                            verify.invoke(null, constructor.newInstance(jarFile.getInputStream(entry)), false, new PrintWriter(System.err));
                        }
                    }
                } catch (NoSuchMethodException e) {
                } catch (InvocationTargetException e) {
                    Thread currentThread = Thread.currentThread();
                    currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, e.getTargetException());
                } catch (Exception e) {
                    Thread currentThread = Thread.currentThread();
                    currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, e);
                }
            }
