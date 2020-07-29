    public static void invokeMvnArtifact(final IProject project, final IModuleExtension moduleExtension, final String location) throws CoreException, InterruptedException, IOException {
        final Properties properties = new Properties();
        properties.put("archetypeGroupId", "org.nexopenframework.plugins");
        properties.put("archetypeArtifactId", "openfrwk-archetype-webmodule");
        final String version = org.maven.ide.eclipse.ext.Maven2Plugin.getArchetypeVersion();
        properties.put("archetypeVersion", version);
        properties.put("artifactId", moduleExtension.getArtifact());
        properties.put("groupId", moduleExtension.getGroup());
        properties.put("version", moduleExtension.getVersion());
        final ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        final ILaunchConfigurationType launchConfigurationType = launchManager.getLaunchConfigurationType(LAUNCH_CONFIGURATION_TYPE_ID);
        final ILaunchConfigurationWorkingCopy workingCopy = launchConfigurationType.newInstance(null, "Creating WEB module using Apache Maven archetype");
        File archetypePomDirectory = getDefaultArchetypePomDirectory();
        try {
            final String dfPom = getPomFile(moduleExtension.getGroup(), moduleExtension.getArtifact());
            final ByteArrayInputStream bais = new ByteArrayInputStream(dfPom.getBytes());
            final File f = new File(archetypePomDirectory, "pom.xml");
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
                } catch (final IOException e) {
                }
            }
            String goalName = "archetype:create";
            boolean offline = false;
            try {
                final Class clazz = Thread.currentThread().getContextClassLoader().loadClass("org.maven.ide.eclipse.Maven2Plugin");
                final Maven2Plugin plugin = (Maven2Plugin) clazz.getMethod("getDefault", new Class[0]).invoke(null, new Object[0]);
                offline = plugin.getPreferenceStore().getBoolean("eclipse.m2.offline");
            } catch (final ClassNotFoundException e) {
                Logger.logException("No class [org.maven.ide.eclipse.ext.Maven2Plugin] in classpath", e);
            } catch (final NoSuchMethodException e) {
                Logger.logException("No method getDefault", e);
            } catch (final Throwable e) {
                Logger.logException(e);
            }
            if (offline) {
                goalName = new StringBuffer(goalName).append(" -o").toString();
            }
            if (!offline) {
                final IPreferenceStore ps = Maven2Plugin.getDefault().getPreferenceStore();
                final String repositories = ps.getString(Maven2PreferenceConstants.P_M2_REPOSITORIES);
                final String[] repos = repositories.split(org.maven.ide.eclipse.ext.Maven2Plugin.REPO_SEPARATOR);
                final StringBuffer sbRepos = new StringBuffer();
                for (int k = 0; k < repos.length; k++) {
                    sbRepos.append(repos[k]);
                    if (k != repos.length - 1) {
                        sbRepos.append(",");
                    }
                }
                properties.put("remoteRepositories", sbRepos.toString());
            }
            workingCopy.setAttribute(ATTR_GOALS, goalName);
            workingCopy.setAttribute(ATTR_POM_DIR, archetypePomDirectory.getAbsolutePath());
            workingCopy.setAttribute(ATTR_PROPERTIES, convertPropertiesToList(properties));
            final long timeout = org.maven.ide.eclipse.ext.Maven2Plugin.getTimeout();
            TimeoutLaunchConfiguration.launchWithTimeout(new NullProgressMonitor(), workingCopy, project, timeout);
            FileUtils.copyDirectoryStructure(new File(archetypePomDirectory, project.getName()), new File(location));
            FileUtils.deleteDirectory(new File(location + "/src"));
            FileUtils.forceDelete(new File(location, "pom.xml"));
            project.refreshLocal(IResource.DEPTH_INFINITE, null);
        } finally {
            FileUtils.deleteDirectory(archetypePomDirectory);
            Logger.log(Logger.INFO, "Invoked removing of archetype POM directory");
        }
    }
