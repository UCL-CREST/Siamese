    protected static URL[] createUrls(URL jarUrls[]) {
        ArrayList<URL> additionalUrls = new ArrayList<URL>(Arrays.asList(jarUrls));
        for (URL ju : jarUrls) {
            try {
                JarFile jar = new JarFile(ju.getFile());
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry j = entries.nextElement();
                    if (j.isDirectory()) continue;
                    if (j.getName().startsWith("lib/") && j.getName().endsWith(".jar")) {
                        URL url = new URL("jar:" + ju.getProtocol() + ":" + ju.getFile() + "!/" + j.getName());
                        InputStream is = url.openStream();
                        File tmpFile = File.createTempFile("SCDeploy", ".jar");
                        FileOutputStream fos = new FileOutputStream(tmpFile);
                        IOUtils.copy(is, fos);
                        is.close();
                        fos.close();
                        additionalUrls.add(new URL("file://" + tmpFile.getAbsolutePath()));
                    }
                }
            } catch (IOException e) {
            }
        }
        return additionalUrls.toArray(new URL[] {});
    }
