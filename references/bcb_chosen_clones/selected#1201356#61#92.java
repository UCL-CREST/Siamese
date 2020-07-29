    private IProject createJavaProject() {
        IProject proj = ResourcesPlugin.getWorkspace().getRoot().getProject("DefaultFolderPropertiesTest");
        if (!proj.exists()) {
            try {
                proj.create(null);
                proj.open(null);
                IProjectDescription desc = proj.getDescription();
                desc.setNatureIds(new String[] { JavaCore.NATURE_ID });
                proj.setDescription(desc, null);
                IJavaProject javaProject = JavaCore.create(proj);
                javaProject.open(null);
                IFolder srcFolder1 = proj.getFolder(new Path("src"));
                srcFolder1.create(true, true, null);
                IFolder srcFolder2 = proj.getFolder(new Path("custom_src"));
                srcFolder2.create(true, true, null);
                IClasspathEntry[] classpathEntries = new IClasspathEntry[] { JavaCore.newSourceEntry(srcFolder1.getFullPath()), JavaCore.newSourceEntry(srcFolder2.getFullPath()), JavaRuntime.getDefaultJREContainerEntry() };
                javaProject.setRawClasspath(classpathEntries, null);
                IFolder binFolder = proj.getFolder(new Path("bin"));
                if (!binFolder.exists()) {
                    binFolder.create(true, true, null);
                }
                javaProject.setOutputLocation(binFolder.getFullPath(), null);
                IFolder testFolder = proj.getFolder(new Path("test"));
                testFolder.create(true, true, null);
                IFolder resultFolder = proj.getFolder(new Path("result"));
                resultFolder.create(true, true, null);
            } catch (CoreException e) {
                fail(e.getMessage());
            }
        }
        return proj;
    }
