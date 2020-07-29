    public void execute() throws MojoExecutionException, MojoFailureException {
        MavenProject project = getProject();
        File file = new File(project.getBuild().getDirectory(), getOutputFile());
        final JarFile jarFile;
        try {
            jarFile = new JarFile(file);
        } catch (IOException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
        Thread thread = new Thread(new Runnable() {

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
        });
        UncaughtExceptionHandler handler = new UncaughtExceptionHandler();
        thread.setUncaughtExceptionHandler(handler);
        @SuppressWarnings("unchecked") Set<Artifact> artifacts = project.getArtifacts();
        List<URL> urls = new ArrayList<URL>(artifacts.size() + 2);
        Iterator<Artifact> iterator = artifacts.iterator();
        try {
            while (iterator.hasNext()) {
                urls.add(iterator.next().getFile().toURI().toURL());
            }
            urls.add(file.toURI().toURL());
            Artifact artifact = artifactFactory.createArtifact("asm", "asm-all", "3.1", null, "jar");
            resolver.resolve(artifact, remoteRepositories, localRepository);
            urls.add(artifact.getFile().toURI().toURL());
        } catch (MalformedURLException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        } catch (ArtifactResolutionException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        } catch (ArtifactNotFoundException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
        thread.setContextClassLoader(new URLClassLoader(urls.toArray(new URL[urls.size()])));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException ex) {
            throw new MojoExecutionException("Execution interrupted", ex);
        }
        Throwable uncaughtException = handler.getUncaughtException();
        if (uncaughtException != null) {
            throw new MojoExecutionException(uncaughtException.getMessage(), uncaughtException);
        }
    }
