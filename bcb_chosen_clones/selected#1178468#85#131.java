    public Class findClass(String className) throws ClassNotFoundException {
        byte[] classByte;
        Class result = null;
        result = (Class) classes.get(className);
        if (result != null) {
            return result;
        }
        try {
            for (File f : classpath) {
                InputStream is;
                if (f.isDirectory()) {
                    File current = new File(f, className.replace('.', File.separatorChar) + ".class");
                    if (!current.exists()) {
                        continue;
                    }
                    is = new FileInputStream(current);
                } else {
                    try {
                        JarFile jar = new JarFile(f);
                        JarEntry entry = jar.getJarEntry(className.replace('.', '/') + ".class");
                        if (entry == null) {
                            continue;
                        }
                        is = jar.getInputStream(entry);
                        if (is == null) {
                            continue;
                        }
                    } catch (Throwable t) {
                        continue;
                    }
                }
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                int nextValue = is.read();
                while (-1 != nextValue) {
                    byteStream.write(nextValue);
                    nextValue = is.read();
                }
                is.close();
                classByte = byteStream.toByteArray();
                result = defineClass(className, classByte, 0, classByte.length, null);
                classes.put(className, result);
                return result;
            }
        } catch (Exception e) {
        }
        return findSystemClass(className);
    }
