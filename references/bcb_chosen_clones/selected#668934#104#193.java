    public void createCodeLocation() {
        List<IClasspathEntry> classpathEntries = new UniqueEList<IClasspathEntry>();
        project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectname);
        try {
            IProjectDescription projectDescription = null;
            IJavaProject javaProject = JavaCore.create(project);
            if (project.exists()) {
                project.delete(true, null);
            }
            projectDescription = ResourcesPlugin.getWorkspace().newProjectDescription(projectname);
            project.create(projectDescription, new NullProgressMonitor());
            String[] natureIds = projectDescription.getNatureIds();
            if (natureIds == null) {
                natureIds = new String[] { JavaCore.NATURE_ID };
            } else {
                boolean hasJavaNature = false;
                boolean hasPDENature = false;
                for (int i = 0; i < natureIds.length; ++i) {
                    if (JavaCore.NATURE_ID.equals(natureIds[i])) {
                        hasJavaNature = true;
                    }
                    if ("org.eclipse.pde.PluginNature".equals(natureIds[i])) {
                        hasPDENature = true;
                    }
                }
                if (!hasJavaNature) {
                    String[] oldNatureIds = natureIds;
                    natureIds = new String[oldNatureIds.length + 1];
                    System.arraycopy(oldNatureIds, 0, natureIds, 0, oldNatureIds.length);
                    natureIds[oldNatureIds.length] = JavaCore.NATURE_ID;
                }
                if (!hasPDENature) {
                    String[] oldNatureIds = natureIds;
                    natureIds = new String[oldNatureIds.length + 1];
                    System.arraycopy(oldNatureIds, 0, natureIds, 0, oldNatureIds.length);
                    natureIds[oldNatureIds.length] = "org.eclipse.pde.PluginNature";
                }
            }
            projectDescription.setNatureIds(natureIds);
            ICommand[] builders = projectDescription.getBuildSpec();
            if (builders == null) {
                builders = new ICommand[0];
            }
            boolean hasManifestBuilder = false;
            boolean hasSchemaBuilder = false;
            for (int i = 0; i < builders.length; ++i) {
                if ("org.eclipse.pde.ManifestBuilder".equals(builders[i].getBuilderName())) {
                    hasManifestBuilder = true;
                }
                if ("org.eclipse.pde.SchemaBuilder".equals(builders[i].getBuilderName())) {
                    hasSchemaBuilder = true;
                }
            }
            if (!hasManifestBuilder) {
                ICommand[] oldBuilders = builders;
                builders = new ICommand[oldBuilders.length + 1];
                System.arraycopy(oldBuilders, 0, builders, 0, oldBuilders.length);
                builders[oldBuilders.length] = projectDescription.newCommand();
                builders[oldBuilders.length].setBuilderName("org.eclipse.pde.ManifestBuilder");
            }
            if (!hasSchemaBuilder) {
                ICommand[] oldBuilders = builders;
                builders = new ICommand[oldBuilders.length + 1];
                System.arraycopy(oldBuilders, 0, builders, 0, oldBuilders.length);
                builders[oldBuilders.length] = projectDescription.newCommand();
                builders[oldBuilders.length].setBuilderName("org.eclipse.pde.SchemaBuilder");
            }
            projectDescription.setBuildSpec(builders);
            project.open(new NullProgressMonitor());
            project.setDescription(projectDescription, new NullProgressMonitor());
            sourceContainer = project.getFolder("src");
            sourceContainer.create(false, true, new NullProgressMonitor());
            IClasspathEntry sourceClasspathEntry = JavaCore.newSourceEntry(new Path("/" + projectname + "/src"));
            classpathEntries.add(0, sourceClasspathEntry);
            String jreContainer = JavaRuntime.JRE_CONTAINER;
            String complianceLevel = CodeGenUtil.EclipseUtil.getJavaComplianceLevel(project);
            if ("1.5".equals(complianceLevel)) {
                jreContainer += "/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.5";
            } else if ("1.6".equals(complianceLevel)) {
                jreContainer += "/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-1.6";
            }
            classpathEntries.add(JavaCore.newContainerEntry(new Path(jreContainer)));
            classpathEntries.add(JavaCore.newContainerEntry(new Path("org.eclipse.pde.core.requiredPlugins")));
            javaProject.setOutputLocation(new Path("/" + projectname + "/bin"), new NullProgressMonitor());
            javaProject.setRawClasspath(classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]), new NullProgressMonitor());
        } catch (CoreException e) {
            e.printStackTrace();
            CodeGenEcorePlugin.INSTANCE.log(e);
        }
    }
