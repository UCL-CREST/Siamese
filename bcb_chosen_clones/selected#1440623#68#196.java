    @SuppressWarnings("unchecked")
    public void launch(final ILaunchConfiguration configuration, final String mode, final ILaunch launch, final IProgressMonitor monitor) throws CoreException {
        {
            Assert.notNull(configuration);
            Assert.notNull(monitor);
        }
        final String projectName = configuration.getAttribute(INexOpenLaunchConfigurationConstants.NEXOPEN_PROJECT_NAME, "");
        final IProject prj = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProject(projectName).getProject();
        if (NexOpenProjectUtils.isNexOpenProject(prj)) {
            final IFile pom = prj.getFile("pom.xml");
            if (!pom.exists()) {
                throw new IllegalStateException("Not a NexOpen project. Not Maven2 root pom.xml available");
            }
            ContentHandlerTemplate.handle(pom, new ContentHandlerCallback() {

                public void processHandle(final Document doc) {
                    handleRootProfile(doc);
                }
            });
            final IFile bpom = prj.getFile("business/pom.xml");
            if (!bpom.exists()) {
                throw new IllegalStateException("Not a NexOpen project. Not Maven2 business pom.xml available");
            }
            ContentHandlerTemplate.handle(bpom, new ContentHandlerCallback() {

                public void processHandle(final Document doc) {
                    try {
                        handleBusinessProfile(doc, configuration, prj);
                    } catch (final CoreException e) {
                        if (Logger.getLog().isInfoEnabled()) {
                            Logger.getLog().info("CoreException", e);
                        }
                        throw new RuntimeException(e);
                    }
                }
            });
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                final Properties props = new Properties();
                final String dialectName = configuration.getAttribute(INexOpenLaunchConfigurationConstants.HIBERNATE_DIALECT, "MySQL5InnoDB");
                props.setProperty("hibernate.dialect", support.getDialectClass(dialectName));
                props.setProperty("hibernate.connection.driver_class", configuration.getAttribute(INexOpenLaunchConfigurationConstants.JDBC_DRIVER, "com.mysql.jdbc.Driver"));
                props.setProperty("hibernate.connection.url", configuration.getAttribute(INexOpenLaunchConfigurationConstants.JDBC_URL, "jdbc:mysql://<host><:port>/<database>"));
                props.setProperty("hibernate.connection.username", configuration.getAttribute(INexOpenLaunchConfigurationConstants.JDBC_USERNAME, "sa"));
                props.setProperty("hibernate.connection.password", configuration.getAttribute(INexOpenLaunchConfigurationConstants.JDBC_PASSWORD, ""));
                props.store(output, "hibernate properties for code generation using NexOpen Tools 1.0.0");
                final IFile props_file = prj.getFile("business/src/test/resources/hibernate.properties");
                if (!props_file.exists()) {
                    props_file.create(new ByteArrayInputStream(output.toByteArray()), true, monitor);
                } else {
                    props_file.setContents(new ByteArrayInputStream(output.toByteArray()), true, false, monitor);
                }
            } catch (final IOException e) {
                Logger.getLog().error("I/O exception ", e);
                throw new RuntimeException(e);
            } finally {
                try {
                    output.flush();
                    output.close();
                } catch (IOException e) {
                }
            }
            if (NexOpenProjectUtils.is04xProject(prj)) {
                final IFile appContext = prj.getFile("web/src/main/webapp/WEB-INF/applicationContext.xml");
                if (!appContext.exists()) {
                    throw new IllegalStateException("It no exists applicationContext.xml under web/src/main/webapp/WEB-INF, not a NexOpen project");
                }
                ContentHandlerTemplate.handle(appContext, new ContentHandlerCallback() {

                    public void processHandle(final Document doc) {
                        final Element root = doc.getDocumentElement();
                        final List<Element> beans = XMLUtils.getChildElementsByTagName(root, "bean");
                        for (final Element bean : beans) {
                            final String id = bean.getAttribute("id");
                            if ("valueListAdapterResolver".equals(id)) {
                                try {
                                    final String pkgName = configuration.getAttribute(INexOpenLaunchConfigurationConstants.NEXOPEN_PACKAGE, "");
                                    final String className = new StringBuilder(pkgName).append(".vlh.support.AnnotationValueListAdapterResolver").toString();
                                    bean.setAttribute("class", className);
                                    break;
                                } catch (final CoreException e) {
                                    if (Logger.getLog().isInfoEnabled()) {
                                        Logger.getLog().info("CoreException", e);
                                    }
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                });
            }
            {
                final String dialectName = configuration.getAttribute(INexOpenLaunchConfigurationConstants.HIBERNATE_DIALECT, "MySQL5InnoDB");
                if (support.isReverseEngineeringFileNeeded(dialectName)) {
                    try {
                        final IFile revengFile = prj.getFile("business/src/test/resources/" + support.getReversEngineeringFile(dialectName));
                        if (!revengFile.exists()) {
                            final Bundle bundle = HibernateActivator.getDefault().getBundle();
                            final Path src = new Path("resources/" + support.getReversEngineeringFile(dialectName));
                            final InputStream in = FileLocator.openStream(bundle, src, false);
                            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            IOUtils.copy(in, baos);
                            String str = baos.toString();
                            str = str.replace("${schema}", configuration.getAttribute(INexOpenLaunchConfigurationConstants.JDBC_USERNAME, "sa"));
                            revengFile.create(new ByteArrayInputStream(str.getBytes()), true, null);
                        }
                    } catch (final IOException e) {
                        if (Logger.getLog().isInfoEnabled()) {
                            Logger.getLog().info("CoreException", e);
                        }
                        throw new RuntimeException(e);
                    }
                }
            }
            final IResource resource = (IResource) prj.getAdapter(IResource.class);
            final QualifiedName qn = new QualifiedName("org.nexopenframework.ide.eclipse.ui", "default.profile");
            final String profile = resource.getPersistentProperty(qn);
            resource.setPersistentProperty(qn, "reverse-engineering");
            try {
                final InstallProjectAction action = new InstallProjectAction();
                action.scheduleJob(prj, monitor);
                prj.refreshLocal(2, monitor);
            } finally {
                prj.setPersistentProperty(qn, profile);
            }
        } else {
            Logger.getLog().info("Not a NexOpen project :: " + prj);
        }
    }
