    public static URL toFileUrl(URL location) throws IOException {
        String protocol = location.getProtocol().intern();
        if (protocol != "jar") throw new IOException("cannot explode " + location);
        JarURLConnection juc = (JarURLConnection) location.openConnection();
        String path = juc.getEntryName();
        String parentPath = parentPathOf(path);
        File tempDir = createTempDir("jartemp");
        JarFile jarFile = juc.getJarFile();
        for (Enumeration<JarEntry> en = jarFile.entries(); en.hasMoreElements(); ) {
            ZipEntry entry = en.nextElement();
            if (entry.isDirectory()) continue;
            String entryPath = entry.getName();
            if (entryPath.startsWith(parentPath)) {
                File dest = new File(tempDir, entryPath);
                dest.getParentFile().mkdirs();
                InputStream in = jarFile.getInputStream(entry);
                OutputStream out = new FileOutputStream(dest);
                IOUtils.copy(in, out);
                dest.deleteOnExit();
            }
        }
        File realFile = new File(tempDir, path);
        return realFile.toURL();
    }
