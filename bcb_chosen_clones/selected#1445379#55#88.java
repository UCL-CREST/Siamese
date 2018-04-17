    public boolean performFinish() {
        try {
            IJavaProject javaProject = JavaCore.create(getProject());
            final IProjectDescription projectDescription = ResourcesPlugin.getWorkspace().newProjectDescription(projectPage.getProjectName());
            projectDescription.setLocation(null);
            getProject().create(projectDescription, null);
            List<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>();
            projectDescription.setNatureIds(getNatures());
            List<String> builderIDs = new ArrayList<String>();
            addBuilders(builderIDs);
            ICommand[] buildCMDS = new ICommand[builderIDs.size()];
            int i = 0;
            for (String builderID : builderIDs) {
                ICommand build = projectDescription.newCommand();
                build.setBuilderName(builderID);
                buildCMDS[i++] = build;
            }
            projectDescription.setBuildSpec(buildCMDS);
            getProject().open(null);
            getProject().setDescription(projectDescription, null);
            addClasspaths(classpathEntries, getProject());
            javaProject.setRawClasspath(classpathEntries.toArray(new IClasspathEntry[classpathEntries.size()]), null);
            javaProject.setOutputLocation(new Path("/" + projectPage.getProjectName() + "/bin"), null);
            createFiles();
            return true;
        } catch (Exception exception) {
            StatusManager.getManager().handle(new Status(IStatus.ERROR, getPluginID(), "Problem creating " + getProjectTypeName() + " project. Ignoring.", exception));
            try {
                getProject().delete(true, null);
            } catch (Exception e) {
            }
            return false;
        }
    }
