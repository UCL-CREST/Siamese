    protected void setUp() throws Exception {
        this.testOutputDirectory = new File(getClass().getResource("/").getPath());
        this.pluginFile = new File(this.testOutputDirectory, "/plugin.zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(pluginFile));
        zos.putNextEntry(new ZipEntry("WEB-INF/"));
        zos.putNextEntry(new ZipEntry("WEB-INF/classes/"));
        zos.putNextEntry(new ZipEntry("WEB-INF/classes/system.properties"));
        System.getProperties().store(zos, null);
        zos.closeEntry();
        zos.putNextEntry(new ZipEntry("WEB-INF/lib/"));
        zos.putNextEntry(new ZipEntry("WEB-INF/lib/plugin.jar"));
        File jarFile = new File(this.testOutputDirectory.getPath() + "/plugin.jar");
        JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarFile));
        jos.putNextEntry(new ZipEntry("vqwiki/"));
        jos.putNextEntry(new ZipEntry("vqwiki/plugins/"));
        jos.putNextEntry(new ZipEntry("vqwiki/plugins/system.properties"));
        System.getProperties().store(jos, null);
        jos.closeEntry();
        jos.close();
        IOUtils.copy(new FileInputStream(jarFile), zos);
        zos.closeEntry();
        zos.close();
        jarFile.delete();
        pcl = new PluginClassLoader(new File(testOutputDirectory, "/work"));
        pcl.addPlugin(pluginFile);
    }
