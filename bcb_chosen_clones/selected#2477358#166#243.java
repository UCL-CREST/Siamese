    private void generateArchetype(final IProject project, final IDataModel model, final IProgressMonitor monitor, final boolean offline) throws CoreException, InterruptedException, IOException {
        if (getArchetypeArtifactId(model) != null) {
            final Properties properties = new Properties();
            properties.put("archetypeArtifactId", getArchetypeArtifactId(model));
            properties.put("archetypeGroupId", getArchetypeGroupId(model));
            properties.put("archetypeVersion", getArchetypeVersion(model));
            String artifact = (String) model.getProperty(IMavenFacetInstallDataModelProperties.PROJECT_ARTIFACT_ID);
            if (artifact == null || artifact.trim().length() == 0) {
                artifact = project.getName();
            }
            properties.put("artifactId", artifact);
            String group = (String) model.getProperty(IMavenFacetInstallDataModelProperties.PROJECT_GROUP_ID);
            if (group == null || group.trim().length() == 0) {
                group = project.getName();
            }
            properties.put("groupId", group);
            properties.put("version", model.getProperty(IMavenFacetInstallDataModelProperties.PROJECT_VERSION));
            final StringBuffer sb = new StringBuffer(System.getProperty("user.home")).append(File.separator);
            sb.append(".m2").append(File.separator).append("repository");
            final String local = sb.toString();
            Logger.getLog().debug("Local Maven2 repository :: " + local);
            properties.put("localRepository", local);
            if (!offline) {
                final String sbRepos = getRepositories();
                properties.put("remoteRepositories", sbRepos);
            }
            final ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
            final ILaunchConfigurationType launchConfigurationType = launchManager.getLaunchConfigurationType(LAUNCH_CONFIGURATION_TYPE_ID);
            final ILaunchConfigurationWorkingCopy workingCopy = launchConfigurationType.newInstance(null, "Creating project using Apache Maven archetype");
            File archetypePomDirectory = getDefaultArchetypePomDirectory();
            try {
                String dfPom = getPomFile(group, artifact);
                ByteArrayInputStream bais = new ByteArrayInputStream(dfPom.getBytes());
                File f = new File(archetypePomDirectory, "pom.xml");
                OutputStream fous = null;
                try {
                    fous = new FileOutputStream(f);
                    IOUtils.copy(bais, fous);
                } finally {
                    try {
                        if (fous != null) {
                            fous.close();
                        }
                        if (bais != null) {
                            bais.close();
                        }
                    } catch (IOException e) {
                    }
                }
                if (SiteManager.isHttpProxyEnable()) {
                    addProxySettings(properties);
                }
                workingCopy.setAttribute(ATTR_POM_DIR, archetypePomDirectory.getAbsolutePath());
                workingCopy.setAttribute(ATTR_PROPERTIES, convertPropertiesToList(properties));
                String goalName = "archetype:create";
                if (offline) {
                    goalName = new StringBuffer(goalName).append(" -o").toString();
                }
                goalName = updateGoal(goalName);
                workingCopy.setAttribute(ATTR_GOALS, goalName);
                final long timeout = org.maven.ide.eclipse.ext.Maven2Plugin.getTimeout();
                TimeoutLaunchConfiguration.launchWithTimeout(monitor, workingCopy, project, timeout);
                monitor.setTaskName("Moving to workspace");
                FileUtils.copyDirectoryStructure(new File(archetypePomDirectory, project.getName()), ArchetypePOMHelper.getProjectDirectory(project));
                monitor.worked(1);
                performMavenInstall(monitor, project, offline);
                project.refreshLocal(2, monitor);
            } catch (final IOException ioe) {
                Logger.log(Logger.ERROR, "I/O exception. One probably solution is absence " + "of mvn2 archetypes or not the correct version, " + "in your local repository. Please, check existence " + "of this archetype.");
                Logger.getLog().error("I/O Exception arised creating mvn2 archetype", ioe);
                throw ioe;
            } finally {
                FileUtils.deleteDirectory(archetypePomDirectory);
                Logger.log(Logger.INFO, "Invoked removing of archetype POM directory");
            }
        }
        monitor.worked(1);
    }
