    public void build(IBuildContext context) throws CoreException {
        try {
            AntlrLanguageToolkit languageToolkit = AntlrLanguageToolkit.getDefault();
            AntlrConsole console = languageToolkit.getConsole();
            AntlrBuildUnit unit = AntlrBuildUnitRepository.create(context);
            if (!unit.canBuild()) {
                return;
            }
            IFile tokenVocabFile = unit.getTokenVocabFile();
            if (tokenVocabFile != null) {
                ISourceModule tokenVocabModule = AntlrSourceParserRepository.getSourceModule(tokenVocabFile);
                SourceModuleBuildContext tokenVocabBuildContext = new SourceModuleBuildContext(new DefaultProblemFactory(), tokenVocabModule, INCREMENTAL_BUILD);
                build(tokenVocabBuildContext);
                BuildProblemReporter reporter = (BuildProblemReporter) tokenVocabBuildContext.getProblemReporter();
                if (reporter != null) {
                    reporter.flush();
                }
            }
            IFile[] dependencies = unit.getDependencies();
            for (IFile file : dependencies) {
                clearMarkers(file);
            }
            preBuild(unit);
            JavaEnvironmentRepository javaEnvironmentRepository = JavaEnvironmentRepositoryLookup.lookup();
            IPath java = javaEnvironmentRepository.getEnvironment(unit.getFile().getProject()).getJavaPath();
            List<String> command = new ArrayList<String>();
            command.add(java.toOSString());
            String memory = unit.getConfiguration().getXmx();
            if (!"0".equals(memory)) {
                command.add("-Xmx" + memory + "m");
            }
            unit.addSystemProperty("gPATTERN", PATTERN);
            unit.addSystemProperty("gLINE_ESCAPE", LINE_ESC_PROPERTY);
            unit.addSystemProperty("gCREUTRNS_ESCAPE", CARRY_RET_ESC_PROPERTY);
            command.addAll(unit.getSystemProperties());
            AntlrDeployer deployer = AntlrDeployerRepository.createDeployer();
            Set<String> hortogonalClasspath = new LinkedHashSet<String>();
            for (IPath deployClasspath : deployer.deployRuntime()) hortogonalClasspath.add(deployClasspath.toOSString());
            hortogonalClasspath.addAll(Arrays.asList(unit.getClasspath().split(File.pathSeparator)));
            StringBuilder classpath = new StringBuilder();
            Iterator<String> classPathIterator = hortogonalClasspath.iterator();
            classpath.append(classPathIterator.next());
            while (classPathIterator.hasNext()) {
                classpath.append(File.pathSeparator);
                classpath.append(classPathIterator.next());
            }
            command.add("-classpath");
            command.add(classpath.toString());
            command.add(unit.getBuildClassName());
            List<String> appcmd = new ArrayList<String>();
            appcmd.addAll(unit.getApplicationArgs());
            appcmd.add("-lib");
            appcmd.add(unit.getAbsoluteLibraryPath().toOSString());
            appcmd.add("-o");
            appcmd.add(unit.getOutputFolder().toOSString());
            appcmd.add(unit.getAbsolutePath().toOSString());
            command.addAll(appcmd);
            String fullpath = unit.getAbsolutePath().toOSString();
            console.info(unit.getDescription());
            console.info("Grammar: " + fullpath);
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(unit.getAbsoluteFolderPath().toFile());
            long startBuild = System.currentTimeMillis();
            Process process = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            IProblemReporter problemReporter = context.getProblemReporter();
            Set<String> problems = new HashSet<String>();
            Map<IPath, IProblemReporter> reporters = new HashMap<IPath, IProblemReporter>() {

                private static final long serialVersionUID = 1L;

                private IProblemFactory problemFactory = new DefaultProblemFactory();

                @Override
                public IProblemReporter get(Object key) {
                    IProblemReporter problemReporter = super.get(key);
                    if (problemReporter == null) {
                        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                        IPath path = (IPath) key;
                        IResource resource = root.getFile(path);
                        problemReporter = new BuildProblemReporter(problemFactory, resource);
                        put(path, problemReporter);
                    }
                    return problemReporter;
                }
            };
            reporters.put(unit.getPath(), problemReporter);
            int errors = 0;
            int warnings = 0;
            String line = in.readLine();
            while (line != null) {
                if (line.startsWith(PATTERN)) {
                    line = AntlrTextHelper.unEscapeNewlines(line);
                    if (problems.add(line)) {
                        String[] message = line.split(PATTERN);
                        AntlrProblem problem = AntlrProblemFactory.create(unit, message);
                        reporters.get(problem.getFilepath()).reportProblem(problem.toDLTKProblem());
                        if (problem.isError()) {
                            errors++;
                        } else {
                            warnings++;
                        }
                        String desc = problem.getRawMessage();
                        console.error(desc);
                        if (problem.getLineWithProblem().length() > 0) {
                            console.error(" |---> " + problem.getLineWithProblem() + "\n");
                        }
                        String key = desc.split("\n")[0];
                        console.setAttribute(key.trim(), problem);
                    }
                } else {
                    if (!fullpath.equals(line)) {
                        console.info(line);
                    }
                }
                line = in.readLine();
            }
            long endBuild = System.currentTimeMillis();
            long buildTime = endBuild - startBuild;
            try {
                in.close();
                process.destroy();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            long seconds = TimeUnit.MILLISECONDS.toSeconds(buildTime);
            if (warnings > 0) {
                console.error(String.format("\n%s warning%s\n", warnings, warnings == 1 ? "" : "s"));
            }
            if (errors == 0) {
                console.info("BUILD SUCCESSFUL");
            } else {
                console.error(String.format("%s error%s\n", errors, errors == 1 ? "" : "s"));
                console.error("BUILD FAIL");
            }
            long time = seconds;
            String timeunit = "second";
            if (time <= 0) {
                time = buildTime;
                timeunit = "millisecond";
            }
            console.info(String.format("Total time: %s %s%s\n", time, timeunit, time == 1 ? "" : "s"));
            postBuild(unit);
        } catch (CoreException ex) {
            throw ex;
        } catch (Exception ex) {
            IStatus status = new Status(IStatus.ERROR, AntlrCore.PLUGIN_ID, "Build fail", ex);
            throw new CoreException(status);
        }
    }
