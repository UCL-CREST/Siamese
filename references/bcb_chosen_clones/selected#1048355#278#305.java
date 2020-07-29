    public static File generateJar(String jarName, Manifest manifest, List<Class> classes, boolean isDeleteOnExit) throws FileNotFoundException, IOException, URISyntaxException {
        File jarDir = calculateJarDir();
        File jarFile = new File(jarDir, jarName);
        JarOutputStream out = null;
        if (manifest == null) {
            out = new JarOutputStream(new FileOutputStream(jarFile));
        } else {
            out = new JarOutputStream(new FileOutputStream(jarFile), manifest);
        }
        for (Iterator<Class> iter = classes.iterator(); iter.hasNext(); ) {
            Class element = iter.next();
            String name = element.getName().replace(".", "/") + ".class";
            InputStream in = ClassLoader.getSystemResourceAsStream(name);
            byte[] bs = new byte[1024];
            int count = 0;
            ZipEntry entry = new ZipEntry(name);
            out.putNextEntry(entry);
            while ((count = in.read(bs)) != -1) {
                out.write(bs, 0, count);
            }
            in.close();
        }
        out.close();
        if (isDeleteOnExit) {
            jarFile.deleteOnExit();
        }
        return jarFile;
    }
