    public void loadPlugins() {
        String pluginFolder = BlueSystem.getUserConfigurationDirectory() + File.separator + "plugins";
        File dir = new File(pluginFolder);
        if (dir.exists() && dir.isDirectory()) {
            File[] jars = dir.listFiles(new FileFilter() {

                public boolean accept(File pathname) {
                    String name = pathname.getName();
                    return name.endsWith(".jar");
                }
            });
            for (int i = 0; i < jars.length; i++) {
                File file = jars[i];
                try {
                    JarFile f = new JarFile(file);
                    System.out.println("Reading Plugins from Jar: " + f.getName());
                    Enumeration entries = f.entries();
                    JarInputStream jis = new JarInputStream(new BufferedInputStream(new FileInputStream(file)));
                    while (entries.hasMoreElements()) {
                        JarEntry entry = (JarEntry) entries.nextElement();
                        if (entry.getName().endsWith(".class")) {
                            InputStream stream = f.getInputStream(entry);
                            ClassByteOutputStream baos = new ClassByteOutputStream(stream);
                            byte[] b = baos.toByteArray();
                            String className = entry.getName();
                            className = className.substring(0, className.length() - 6);
                            className = className.replaceAll("/", ".");
                            Class c = defineClass(className, b, 0, b.length);
                            classes.put(className, c);
                            Class[] interfaces = c.getInterfaces();
                            if (NoteProcessor.class.isAssignableFrom(c)) {
                                System.out.println("Found NoteProcessor: " + className);
                                noteProcessors.add(c);
                            }
                            if (SoundObject.class.isAssignableFrom(c)) {
                                System.out.println("Found SoundObject: " + className);
                                soundObjects.add(c);
                            }
                            if (Instrument.class.isAssignableFrom(c)) {
                                System.out.println("Found Instrument: " + className);
                                instruments.add(c);
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
