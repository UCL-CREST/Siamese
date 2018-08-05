    private void putAlgosFromJar(File jarfile, AlgoDir dir, Model model) throws FileNotFoundException, IOException {
        URLClassLoader urlLoader = new URLClassLoader(new URL[] { jarfile.toURI().toURL() });
        JarInputStream jis = new JarInputStream(new FileInputStream(jarfile));
        JarEntry entry = jis.getNextJarEntry();
        String name = null;
        String tmpdir = System.getProperty("user.dir") + File.separator + Application.getProperty("dir.tmp") + File.separator;
        byte[] buffer = new byte[1000];
        while (entry != null) {
            name = entry.getName();
            if (name.endsWith(".class")) {
                name = name.substring(0, name.length() - 6);
                name = name.replace('/', '.');
                try {
                    Class<?> cls = urlLoader.loadClass(name);
                    if (IAlgorithm.class.isAssignableFrom(cls) && !cls.isInterface() && ((cls.getModifiers() & Modifier.ABSTRACT) == 0)) {
                        dir.addAlgorithm(cls);
                        model.putClass(cls.getName(), cls);
                    } else if (ISerializable.class.isAssignableFrom(cls)) {
                        model.putClass(cls.getName(), cls);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (Constants.isAllowedImageType(name)) {
                int lastSep = name.lastIndexOf("/");
                if (lastSep != -1) {
                    String dirs = tmpdir + name.substring(0, lastSep);
                    File d = new File(dirs);
                    if (!d.exists()) d.mkdirs();
                }
                String filename = tmpdir + name;
                File f = new File(filename);
                if (!f.exists()) {
                    f.createNewFile();
                    FileOutputStream fos = new FileOutputStream(f);
                    int read = -1;
                    while ((read = jis.read(buffer)) != -1) {
                        fos.write(buffer, 0, read);
                    }
                    fos.close();
                }
            }
            entry = jis.getNextJarEntry();
        }
    }
