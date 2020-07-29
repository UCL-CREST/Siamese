            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                int remainingWorkUnits = 10;
                monitor.beginTask("New Modulo Project Creation", remainingWorkUnits);
                IWorkspace ws = ResourcesPlugin.getWorkspace();
                newProject = fMainPage.getProjectHandle();
                IProjectDescription description = ws.newProjectDescription(newProject.getName());
                String[] natures = { JavaCore.NATURE_ID, ModuloLauncherPlugin.NATURE_ID };
                description.setNatureIds(natures);
                ICommand command = description.newCommand();
                command.setBuilderName(JavaCore.BUILDER_ID);
                ICommand[] commands = { command };
                description.setBuildSpec(commands);
                IJavaProject jproject = JavaCore.create(newProject);
                ModuloProject modProj = new ModuloProject();
                modProj.setJavaProject(jproject);
                try {
                    newProject.create(description, new SubProgressMonitor(monitor, 1));
                    newProject.open(new SubProgressMonitor(monitor, 1));
                    IFolder srcFolder = newProject.getFolder("src");
                    IFolder javaFolder = srcFolder.getFolder("java");
                    IFolder buildFolder = newProject.getFolder("build");
                    IFolder classesFolder = buildFolder.getFolder("classes");
                    modProj.createFolder(srcFolder);
                    modProj.createFolder(javaFolder);
                    modProj.createFolder(buildFolder);
                    modProj.createFolder(classesFolder);
                    IPath buildPath = newProject.getFolder("build/classes").getFullPath();
                    jproject.setOutputLocation(buildPath, new SubProgressMonitor(monitor, 1));
                    IClasspathEntry[] entries = new IClasspathEntry[] { JavaCore.newSourceEntry(newProject.getFolder("src/java").getFullPath()), JavaCore.newContainerEntry(new Path(JavaRuntime.JRE_CONTAINER)), JavaCore.newContainerEntry(new Path(ModuloClasspathContainer.CONTAINER_ID)) };
                    jproject.setRawClasspath(entries, new SubProgressMonitor(monitor, 1));
                    ModuleDefinition definition = new ModuleDefinition();
                    definition.setId(fModuloPage.getPackageName());
                    definition.setVersion(new VersionNumber(1, 0, 0));
                    definition.setMetaName(fModuloPage.getModuleName());
                    definition.setMetaDescription("The " + fModuloPage.getModuleName() + " Module.");
                    definition.setModuleClassName(fModuloPage.getPackageName() + "." + fModuloPage.getModuleClassName());
                    if (fModuloPage.isConfigSelectioned()) definition.setConfigurationClassName(fModuloPage.getPackageName() + "." + fModuloPage.getConfigClassName());
                    if (fModuloPage.isStatSelectioned()) definition.setStatisticsClassName(fModuloPage.getPackageName() + "." + fModuloPage.getStatClassName());
                    modProj.setDefinition(definition);
                    modProj.createPackage();
                    modProj.createModuleXML();
                    modProj.createMainClass();
                    if (fModuloPage.isConfigSelectioned()) modProj.createConfigClass();
                    if (fModuloPage.isStatSelectioned()) modProj.createStatClass();
                    modProj.createModuleProperties();
                    modProj.createMessagesProperties();
                    IFolder binFolder = newProject.getFolder("bin");
                    binFolder.delete(true, new SubProgressMonitor(monitor, 1));
                } catch (CoreException e) {
                    e.printStackTrace();
                } finally {
                    monitor.done();
                }
            }
