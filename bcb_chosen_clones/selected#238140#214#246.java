    public static void createJar(File jarFile, String[] classpath, LinkedList<File> files) throws IOException {
        System.out.println("Creating jar file: " + jarFile);
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile));
        for (File f : files) {
            String name = f.getPath();
            if (classpath != null) {
                String[] names = new String[classpath.length];
                for (int i = 0; i < classpath.length; i++) {
                    String path = new File(classpath[i]).getCanonicalPath();
                    if (name.startsWith(path)) {
                        names[i] = name.substring(path.length() + 1);
                    }
                }
                for (int i = 0; i < names.length; i++) {
                    if (names[i] != null && names[i].length() < name.length()) {
                        name = names[i];
                    }
                }
            }
            ZipEntry e = new ZipEntry(name.replace(File.separatorChar, '/'));
            e.setTime(f.lastModified());
            jos.putNextEntry(e);
            FileInputStream fis = new FileInputStream(f);
            byte[] data = new byte[fis.available()];
            int n;
            while ((n = fis.read(data)) > 0) {
                jos.write(data, 0, n);
            }
            fis.close();
            jos.closeEntry();
        }
        jos.close();
    }
