    private void addTestJar() throws Exception {
        String jarLocation = getFile("test.jar").getLocation().toPortableString();
        {
            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(jarLocation));
            jarOutputStream.putNextEntry(new ZipEntry("jar/folder/hello.txt"));
            jarOutputStream.write("Hello!".getBytes());
            jarOutputStream.closeEntry();
            jarOutputStream.close();
        }
        m_javaProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
        ProjectUtils.addExternalJar(m_javaProject, jarLocation, null);
        waitForAutoBuild();
    }
