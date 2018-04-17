    private void unJarStart(String jarPath, String jarEntryStart) {
        String path;
        if (jarPath.lastIndexOf("lib/") >= 0) path = jarPath.substring(0, jarPath.lastIndexOf("lib/")); else path = jarPath.substring(0, jarPath.lastIndexOf("/"));
        String relPath = jarEntryStart.substring(0, jarEntryStart.lastIndexOf("/"));
        try {
            new File(path + "/" + relPath).mkdirs();
            JarFile jar = new JarFile(jarPath);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String jarEntry = entry.getName();
                if (jarEntry.startsWith(jarEntryStart)) {
                    ZipEntry ze = jar.getEntry(jarEntry);
                    File bin = new File(path + "/" + jarEntry);
                    IOUtils.copy(jar.getInputStream(ze), new FileOutputStream(bin));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
