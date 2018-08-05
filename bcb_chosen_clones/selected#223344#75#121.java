    private void createProject(IProgressMonitor monitor, boolean launchNewTestWizard) {
        try {
            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
            IProject project = root.getProject(namePage.getProjectName());
            IProjectDescription description = ResourcesPlugin.getWorkspace().newProjectDescription(project.getName());
            if (!Platform.getLocation().equals(namePage.getLocationPath())) description.setLocation(namePage.getLocationPath());
            description.setNatureIds(new String[] { JavaCore.NATURE_ID });
            ICommand buildCommand = description.newCommand();
            buildCommand.setBuilderName(JavaCore.BUILDER_ID);
            description.setBuildSpec(new ICommand[] { buildCommand });
            project.create(description, monitor);
            project.open(monitor);
            IJavaProject javaProject = JavaCore.create(project);
            IFolder testFolder = project.getFolder("tests");
            testFolder.create(false, true, monitor);
            IFolder srcFolder = project.getFolder("src");
            srcFolder.create(false, true, monitor);
            IFolder binFolder = project.getFolder("bin");
            binFolder.create(false, true, monitor);
            IFolder libFolder = project.getFolder("lib");
            libFolder.create(false, true, monitor);
            try {
                FileUtils.copyFile(new Path(Platform.asLocalURL(CubicTestPlugin.getDefault().find(new Path("lib/CubicTestElementAPI.jar"))).getPath()).toFile(), libFolder.getFile("CubicTestElementAPI.jar").getLocation().toFile());
                FileUtils.copyFile(new Path(Platform.asLocalURL(CubicTestPlugin.getDefault().find(new Path("lib/CubicUnit.jar"))).getPath()).toFile(), libFolder.getFile("CubicUnit.jar").getLocation().toFile());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            javaProject.setOutputLocation(binFolder.getFullPath(), monitor);
            IClasspathEntry[] classpath;
            classpath = new IClasspathEntry[] { JavaCore.newSourceEntry(srcFolder.getFullPath()), JavaCore.newContainerEntry(new Path("org.eclipse.jdt.launching.JRE_CONTAINER")), JavaCore.newLibraryEntry(libFolder.getFile("CubicTestElementAPI.jar").getFullPath(), null, null), JavaCore.newLibraryEntry(libFolder.getFile("CubicUnit.jar").getFullPath(), null, null) };
            javaProject.setRawClasspath(classpath, binFolder.getFullPath(), monitor);
            ResourceNavigator navigator = null;
            IViewPart viewPart = workbench.getActiveWorkbenchWindow().getActivePage().getViewReferences()[0].getView(false);
            if (viewPart instanceof ResourceNavigator) {
                navigator = (ResourceNavigator) viewPart;
            }
            if (launchNewTestWizard) {
                launchNewTestWizard(testFolder);
                if (navigator != null && testFolder.members().length > 0) {
                    navigator.selectReveal(new StructuredSelection(testFolder.members()[0]));
                }
            }
            project.refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }
