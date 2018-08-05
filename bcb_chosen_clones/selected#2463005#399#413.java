    private String unJar(String jarPath, String jarEntry) {
        String path;
        if (jarPath.lastIndexOf("lib/") >= 0) path = jarPath.substring(0, jarPath.lastIndexOf("lib/")); else path = jarPath.substring(0, jarPath.lastIndexOf("/"));
        String relPath = jarEntry.substring(0, jarEntry.lastIndexOf("/"));
        try {
            new File(path + "/" + relPath).mkdirs();
            JarFile jar = new JarFile(jarPath);
            ZipEntry ze = jar.getEntry(jarEntry);
            File bin = new File(path + "/" + jarEntry);
            IOUtils.copy(jar.getInputStream(ze), new FileOutputStream(bin));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path + "/" + jarEntry;
    }
