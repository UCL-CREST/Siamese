    public static void main(String[] args) {
        File container = new File(ArchiveFeature.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        if (container == null) {
            throw new RuntimeException("this use-case isn't being invoked from the executable jar");
        }
        JarFile jarFile = new JarFile(container);
        String artifactName = PROJECT_DIST_ARCHIVE + ".tar.gz";
        File artifactFile = new File(".", artifactName);
        ZipEntry artifactEntry = jarFile.getEntry(artifactName);
        InputStream source = jarFile.getInputStream(artifactEntry);
        try {
            FileOutputStream dest = new FileOutputStream(artifactFile);
            try {
                IOUtils.copy(source, dest);
            } finally {
                IOUtils.closeQuietly(dest);
            }
        } finally {
            IOUtils.closeQuietly(source);
        }
        Project project = new Project();
        project.setName("project");
        project.init();
        Target target = new Target();
        target.setName("target");
        project.addTarget(target);
        project.addBuildListener(new Log4jListener());
        Untar untar = new Untar();
        untar.setTaskName("untar");
        untar.setSrc(artifactFile);
        untar.setDest(new File("."));
        Untar.UntarCompressionMethod method = new Untar.UntarCompressionMethod();
        method.setValue("gzip");
        untar.setCompression(method);
        untar.setProject(project);
        untar.setOwningTarget(target);
        target.addTask(untar);
        untar.execute();
    }
